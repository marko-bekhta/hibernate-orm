module org.hibernate.orm.scan.jandex {
	requires jakarta.persistence;
	requires org.jboss.jandex;

	requires transitive org.hibernate.orm.core;

	exports org.hibernate.archive.scan.internal;
	exports org.hibernate.archive.scan.spi;

	provides org.hibernate.boot.archive.scan.spi.ScannerFactory with
		org.hibernate.archive.scan.internal.StandardScannerFactory;

}
