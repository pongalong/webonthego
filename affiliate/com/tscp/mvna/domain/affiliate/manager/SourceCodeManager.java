package com.tscp.mvna.domain.affiliate.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tscp.mvna.domain.affiliate.SourceCode;
import com.tscp.mvna.domain.affiliate.manager.dao.SourceCodeDao;

@Component
public class SourceCodeManager {
	@Autowired
	private SourceCodeDao sourceCodeDao;

	public int save(
			SourceCode sourceCode) {
		if (sourceCode.getParent() != null && sourceCode.getParent().getId() == 0) {
			sourceCode.setParent(null);
		}
		return sourceCodeDao.save(sourceCode);
	}

	public void update(
			SourceCode sourceCode) {
		if (sourceCode.getParent() != null && sourceCode.getParent().getId() == 0) {
			sourceCode.setParent(null);
		}
		sourceCodeDao.update(sourceCode);
	}

	public void delete(
			SourceCode sourceCode) {
		sourceCodeDao.delete(sourceCode);
	}

	public SourceCode get(
			int id) {
		return sourceCodeDao.get(id);
	}

	public SourceCode getByCode(
			String code) {
		return sourceCodeDao.getByCode(code);
	}

	public List<SourceCode> getAll() {
		return sourceCodeDao.getAll();
	}

	public List<SourceCode> searchByCode(
			String code) {
		return sourceCodeDao.searchByCode(code);
	}

}
