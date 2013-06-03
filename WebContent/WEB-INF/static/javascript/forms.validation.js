/*******************************************************************************
 * NAMESPACE DECLARATION
 * 
 * Currenty unused but this is the framework for form validation using object
 * and namespaces to separate functions
 ******************************************************************************/
var forms = forms || {};

forms.validation = forms.validation || {};

/*******************************************************************************
 * Validation Methods
 ******************************************************************************/

forms.validation.validateChecked = function(value) {
	return value == "true";
};

forms.validation.validateSelected = function(value) {
	return value != 0;
};

forms.validation.validateNotEmpty = function(value) {
	return value != null && $.trim(value) != "";
};

forms.validation.validateMatches = function(value1, value2) {
	return value1 != "" && value1 == value2;
};

forms.validation.validateAlphaNumeric = function(value) {
	var alphaNumRegex = /^(?:[0-9]+[a-z]|[a-z]+[0-9])[a-z0-9]*$/i;
	return alphaNumRegex.test(value);
};

forms.validation.validatePassword = function(value) {
	return validateAlphaNumeric(value);
};

forms.validation.validateEmail = function(email) {
	var emailRegex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA	-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	return emailRegex.test(email);
};