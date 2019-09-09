package es.test;

import javax.sql.DataSource;

public interface DataSourceLocator {

    DataSource getDataSource() throws Exception;
}
