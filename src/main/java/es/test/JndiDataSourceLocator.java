package es.test;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class JndiDataSourceLocator implements DataSourceLocator {

	private final String jndiName;
	private DataSource dataSource;

	public JndiDataSourceLocator(String jndiName) {
		this.jndiName = jndiName;
	}

	@Override
	public DataSource getDataSource() throws Exception {
		if (dataSource == null) {
			InitialContext context = new InitialContext();
			dataSource = (DataSource) context.lookup(jndiName);
		}
		return null;
	}

	public static class FirstJndi extends JndiDataSourceLocator {
		public FirstJndi() {
			super("");
		}
	}
}
