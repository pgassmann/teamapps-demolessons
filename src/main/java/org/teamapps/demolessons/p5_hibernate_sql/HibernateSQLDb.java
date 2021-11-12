package org.teamapps.demolessons.p5_hibernate_sql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.teamapps.demolessons.p5_hibernate_sql.models.Customer;
import org.teamapps.demolessons.p5_hibernate_sql.models.Invoice;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HibernateSQLDb {
	private static HibernateSQLDb singleton;
	private final SessionFactory sessionFactory;

	private HibernateSQLDb() {
		String path = "./server-data";
		try {
			Files.createDirectory(java.nio.file.Path.of(path));
		} catch (FileAlreadyExistsException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}

		Configuration cfg = new Configuration()
				.addAnnotatedClass(Customer.class)
				.addAnnotatedClass(Invoice.class)
				.setProperty("hibernate.hbm2ddl.auto", "update")
				.setProperty("hibernate.connection.url", "jdbc:sqlite:" + path + "/p5_hibernate_sql.db");
//		cfg.setProperty("show_sql", "true");
//		BasicConfigurator.configure();
//		Logger.getLogger("org.hibernate.SQL").setLevel(Level.TRACE);

		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();

		sessionFactory = cfg.buildSessionFactory(serviceRegistry);
	}

	public static HibernateSQLDb getInstance() {
		if (singleton == null) {
			singleton = new HibernateSQLDb();
		}
		return singleton;
	}

	public Session createSession() {
		return sessionFactory.openSession();
	}

	public void ensureInitialized() {
		Session session = createSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root root = cq.from(Invoice.class);
		cq.select(cb.count(root));
		if (session.createQuery(cq).getSingleResult() == 0) {
			final String[] forenames = {"James", "John", "Robert", "Michael", "David", "Mary", "Patricia", "Linda", "Barbara", "Elizabeth"};
			final String[] surnames = {"Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor"};

			session.beginTransaction();

			List<Customer> customers = new ArrayList<>();
			for (String forename : forenames) {
				for (String surname : surnames) {
					Customer c = new Customer();
					c.setForename(forename);
					c.setSurname(surname);
					c.setEmail(String.format("%s@%s.com", forename, surname));
					session.save(c);
					customers.add(c);
				}
			}

			Random rand = new Random();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy");
			LocalDateTime baseTime = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).minusMonths(1).atStartOfDay();
			for (int k = 0; k < 500; k++) {
				Invoice i = new Invoice();
				i.setIdentifier(String.format("%s/%d", dateTimeFormatter.format(baseTime), 1001 + k));
				i.setCustomer(customers.get(rand.nextInt(customers.size())));
				i.setSum(rand.nextInt(1000 * 100) * 0.01);
				i.setCreated(baseTime);
				session.save(i);
				baseTime = baseTime.plusHours(6);
			}

			session.getTransaction().commit();
		}
	}
}
