package es.test;

import javax.sql.DataSource;

public class OtherDataSourceLocator implements DataSourceLocator {

	private DataSource dataSource;

	public OtherDataSourceLocator() {
	}

	@Override
	public DataSource getDataSource() throws Exception {
		if (dataSource == null) {
			dataSource = null;
		}
		return null;
	}
}
