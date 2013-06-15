var upKey = 38;
var downKey = 40;
var enterKey = 13;
var requestBufferTime;
var currentRequest;

var htmlLoadingGraphic = "<div class='loading-msg'>Searching...<br/><img src='/static/images/util/ajax_working_bar.gif' /></div>";

var userSearch = {};
var sourceCodeSearch = {};

/**
 * Initialize search variables.
 */
$(function() {
	userSearch.targetUrl = "/search/getjson/user/email/ajax";
	userSearch.selected = null;
	userSearch.selectedIndex = -1;
	userSearch.focus = false;
	userSearch.keepFocus = false;
	userSearch.param = $("#admin-search-param");
	userSearch.param.id = $("#admin-search-param-id");
	userSearch.param.desc = $("#admin-searh-param-username");
	userSearch.button = $("#admin-search-submit");
	userSearch.reset = $("#admin-search-reset");
	userSearch.resultsCanvas = $("#admin-search-results-container");
	userSearch.resultsList = $("#admin-search-results");
	userSearch.results = $("#admin-search-results-container .result");

	sourceCodeSearch.targetUrl = "/search/getjson/sourcecode/code/ajax";
	sourceCodeSearch.selected = null;
	sourceCodeSearch.selectedIndex = -1;
	sourceCodeSearch.focus = false;
	sourceCodeSearch.keepFocus = false;
	sourceCodeSearch.param = $("#sourcecode-search-param");
	sourceCodeSearch.param.id = $("#id");
	sourceCodeSearch.param.desc = $("#name");
	sourceCodeSearch.resultsCanvas = $("#sourcecode-search-results-container");
	sourceCodeSearch.resultsList = $("#sourcecode-search-results");
	sourceCodeSearch.results = $("#sourcecode-search-results-container .result");
	sourceCodeSearch.button = $("#sourcecode-search-select-button");
});

/**
 * Initialize search box behaviors
 */
$(function() {
	setupButtons(userSearch);
	setupFocus(userSearch);
	setupKeyUp(userSearch);
	setupKeyDown(userSearch);

	setupFocus(sourceCodeSearch);
	setupKeyUp(sourceCodeSearch);
	setupKeyDown(sourceCodeSearch);

	// Special behavior for source code box
	// hide the menu and lose focus on selection
	sourceCodeSearch.button.click(function() {
		sourceCodeSearch.focus = false;
		sourceCodeSearch.keepFocus = false;
		sourceCodeSearch.resultsCanvas.toggleSlideUp();
	});
});

/*******************************************************************************
 * Buffered getJSON Methods
 */

function searchRequest(searchSpace) {
	if (currentRequest == null) {
		loadResults(searchSpace);
		currentRequest = "complete";
	} else {
		searchSpace.resultsList.html(htmlLoadingGraphic);
		setTimeout(function() {
			bufferAjaxRequest(searchSpace);
		}, 1000);
	}
}

function bufferAjaxRequest(searchSpace) {
	requestBufferTime--;
	currentRequest = requestBufferTime == 0 ? null : currentRequest;
	searchRequest(searchSpace);
}

function loadResults(searchSpace) {
	$.getJSON(searchSpace.targetUrl, {
		param : searchSpace.param.val()
	}, function(searchResponse) {
		if (searchResponse.success) {

			// Build the list of results
			var response = "<ul class='results-list'>";
			for ( var i = 0; i < searchResponse.results.length; i++)
				response += "<li class='result'><span class='id'>" + searchResponse.results[i].id + "</span><span class='value'>"
						+ searchResponse.results[i].label + "</span><span class='desc'>" + searchResponse.results[i].description + "</span></li>";
			response += "</ul>";

			// Add the list of results to the searchSpace canvas
			searchSpace.resultsList.html(response);

			// Set the namespace of fetched results
			searchSpace.results = $(".results-list .result");

			// Set mouseover behavior of fetched results
			searchSpace.results.mouseover(function() {
				searchSpace.selectedIndex = $(this).index();
				searchSpace.selected = $(this);
				$(this).highlightSelected();
				searchSpace.results.not($(this)).unhighlightSelected();
			}).click(function() {
				searchSpace.selected.select(searchSpace);
				searchSpace.button.click();
			});

		} else {
			searchSpace.resultsList.html("No results");
		}
		currentRequest = null;
	});
}

/*******************************************************************************
 * Navigation and Selection Helper Methods
 */

function isNavigationKey(e) {
	return (e.keyCode >= 37 && e.keyCode <= 40) || e.keyCode == 13;
}

function isDeleteKey(e) {
	return e.keyCode == 8 || e.keyCode == 46;
}

function selectFirstResult(searchSpace) {
	searchSpace.selectedIndex = 0;
	searchSpace.selected = searchSpace.results.eq(0);
	searchSpace.selected.highlightSelected();
}

function selectAndHighlight(searchSpace) {
	searchSpace.selected = searchSpace.results.eq(searchSpace.selectedIndex);
	searchSpace.selected.highlightSelected();
	searchSpace.results.not(searchSpace.selected).unhighlightSelected();
}

$.fn.highlightSelected = function() {
	this.css("background", "#71C0F5");
};

$.fn.unhighlightSelected = function() {
	this.css("background", "");
};

$.fn.navUp = function(searchSpace) {
	if (searchSpace.selectedIndex > 0) {
		searchSpace.selectedIndex--;
		selectAndHighlight(searchSpace);
	} else {
		searchSpace.selected = null;
		searchSpace.selectedIndex = -1;
		searchSpace.results.unhighlightSelected();
	}
};

$.fn.navDown = function(searchSpace) {
	if (searchSpace.selectedIndex < searchSpace.results.size() - 1) {
		searchSpace.selectedIndex++;
		selectAndHighlight(searchSpace);
	}
};

$.fn.select = function(searchSpace) {
	searchSpace.param.val(this.children(".value").html());
	searchSpace.param.id.val(this.children(".id").html());
	if (searchSpace.param.desc.exists())
		searchSpace.param.desc.val(this.children(".desc").html());
	searchSpace.button.click();
};

/*******************************************************************************
 * User Search Behavior Methods
 */

function setupButtons(searchSpace) {
	searchSpace.reset.click(function() {
		searchSpace.param.id.val(0);
		searchSpace.button.click();
	});
}

function setupFocus(searchSpace) {
	searchSpace.param.focus(function() {
		searchSpace.focus = true;
		if (searchSpace.param.val() != "") {
			searchSpace.resultsCanvas.toggleSlideDownRemainingHeight();
		}
	});
	searchSpace.param.blur(function() {
		searchSpace.focus = false;
		if (!searchSpace.keepFocus) {
			searchSpace.resultsCanvas.toggleSlideUp();
		}
	});
	searchSpace.resultsCanvas.mouseenter(function() {
		searchSpace.keepFocus = true;
		searchSpace.param.focus();
	});
	searchSpace.resultsCanvas.mouseleave(function() {
		searchSpace.keepFocus = false;
		// searchSpace.param.focus();
	});
}

function setupKeyUp(searchSpace) {
	searchSpace.param.keyup(function(e) {
		if (!isNavigationKey(e) && $(this).val() != "") {
			searchSpace.resultsCanvas.toggleSlideDownRemainingHeight();
			requestBufferTime = 2;
			if (currentRequest == null) {
				currentRequest = "pending";
				searchRequest(searchSpace);
			}
		} else if (isDeleteKey(e) && $(this).val() == "") {
			searchSpace.resultsCanvas.toggleSlideUp();
		}
	});
}

function setupKeyDown(searchSpace) {
	searchSpace.param.keydown(function(e) {
		if (isNavigationKey(e)) {
			e.preventDefault();
			if (e.keyCode == downKey) {
				if (searchSpace.selected == null) {
					selectFirstResult(searchSpace);
				} else {
					searchSpace.selected.navDown(searchSpace);
				}
			} else if (e.keyCode == upKey) {
				searchSpace.selected.navUp(searchSpace);
			} else if (e.keyCode == enterKey) {
				searchSpace.selected.select(searchSpace);
			}
		}
	});
}
