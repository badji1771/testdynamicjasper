package bceao.poc.dynamicjasper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.LayoutManager;
import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.util.SortUtils;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

public class Main {

	protected static final Log log = LogFactory.getLog(Main.class);

	protected JasperPrint jp;
	protected JasperReport jr;
	protected FastReportBuilder frb = new FastReportBuilder();
	protected final Map<String, Object> params = new HashMap<String, Object>();

	public static List<String> getTrimestre() {
		List<String> lt = new ArrayList<String>();
		lt.add("Trimestre1");
		lt.add("Trimestre2");
		lt.add("Trimestre3");
		lt.add("Trimestre4");
		lt.add("Trimestre5");
		lt.add("Trimestre6");
		lt.add("Trimestre7");
		lt.add("Trimestre8");
		return lt;
	}

	public static List<Product> getDummyCollection() {

		SimpleDateFormat dateFormat = new SimpleDateFormat();

		dateFormat.applyPattern("dd/MM/yyyy");

		List<Product> col = new ArrayList<Product>();

		// The collection is ordered by State, Branch and Product Line

		col.add(new Product(new Long("1"), "book", "Harry Potter 7", "Florida", "Main Street", new Long("2500"),
				new Float("10000")));

		col.add(new Product(new Long("1"), "book", "Harry Potter 7", "Florida", "Railway Station", new Long("1400"),
				new Float("2831.32")));

		col.add(new Product(new Long("1"), "book", "Harry Potter 7", "Florida", "Baseball Stadium", new Long("4000"),
				new Float("38347")));

		col.add(new Product(new Long("1"), "book", "Harry Potter 7", "Florida", "Shopping Center", new Long("3000"),
				new Float("9482.4")));

		col.add(new Product(new Long("1"), "book", "Harry Potter 7", "New York", "Main Street", new Long("2500"),
				new Float("27475.5")));

		col.add(new Product(new Long("1"), "book", "Harry Potter 7", "New York", "Railway Station", new Long("1400"),
				new Float("3322")));

		col.add(new Product(new Long("1"), "book", "Harry Potter 7", "New York", "Baseball Stadium", new Long("4000"),
				new Float("78482")));

		col.add(new Product(new Long("1"), "book", "Harry Potter 7", "New York", "Shopping Center", new Long("3000"),
				new Float("5831.32")));

		col.add(new Product(new Long("2"), "book", "The Sum of All Fears", "New York", "Main Street", new Long("1500"),
				new Float("8329.2")));

		col.add(new Product(new Long("2"), "book", "The Sum of All Fears", "New York", "Railway Station",
				new Long("2500"), new Float("27475.5")));

		col.add(new Product(new Long("3"), "book", "The Pelican Brief,", "New York", "Main Street", new Long("2500"),
				new Float("38347")));

		col.add(new Product(new Long("3"), "book", "The Pelican Brief,", "New York", "Railway Station",
				new Long("1400"), new Float("9482.4")));

		col.add(new Product(new Long("3"), "book", "The Pelican Brief,", "New York", "Baseball Stadium",
				new Long("1500"), new Float("8329.2")));

		col.add(new Product(new Long("3"), "book", "The Pelican Brief,", "New York", "Shopping Center",
				new Long("2500"), new Float("27475.5")));

		col.add(new Product(new Long("3"), "book", "The Pelican Brief,", "Washington", "Main Street", new Long("1400"),
				new Float("3322")));
		return col;

	}

	public void getParam(String trimestreDepart, List<String> listTrimestre,Integer nbrTrimestre, String titre, String sousTitre) throws Exception {
		int j = 1;
		frb.addColumn("Pays", "state", String.class.getName(), 30);
		frb.setTitle(titre);
		frb.setSubtitle(sousTitre + " :" + new Date());
		frb.setColumnsPerPage(1, 10);
		frb.setUseFullPageWidth(true);

		for (int i = 0; i < nbrTrimestre; i++) {
			frb.addColumn("item1", "amount", Float.class.getName(), 30);
			frb.addColumn("item2", "branch", String.class.getName(), 30);
			frb.addColumn("item3", "item", String.class.getName(), 30);
		}
		for (String string : listTrimestre) {
			Integer index =listTrimestre.indexOf(string);
			if (listTrimestre.indexOf(trimestreDepart) <= listTrimestre.indexOf(string) ) {
				if(index < (nbrTrimestre+listTrimestre.indexOf(trimestreDepart))){
					frb.setColspan(j, 3, string);
				}
				j = j + 3;
			}
		}
		//frb.setColumnsPerPage (2,10);
		frb.setUseFullPageWidth(true);
		frb.addAutoText(AutoText.AUTOTEXT_PAGE_X_SLASH_Y, AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_RIGHT);
	}

	public void construireReport(JRDataSource ds, DynamicReport dr) throws Exception {
		/**
		 * Creates the JasperReport object, we pass as a Parameter the
		 * DynamicReport, a new ClassicLayoutManager instance (this one does the
		 * magic) and the JRDataSource
		 */
		jr = DynamicJasperHelper.generateJasperReport(dr, getLayoutManager(), params);

		/**
		 * Creates the JasperPrint object, we pass as a Parameter the
		 * JasperReport object, and the JRDataSource
		 */
		log.debug("Filling the report");
		if (ds != null)
			jp = JasperFillManager.fillReport(jr, params, ds);
		else
			jp = JasperFillManager.fillReport(jr, params);
		log.debug("Filling done!");
		log.debug("Exporting the report (pdf, xls, etc)");
		log.debug("test finished");
	}

	public DynamicReport buildReportTemplate() throws Exception {
		DynamicReport dynamicReport = frb.build();
		dynamicReport.getOptions().getDefaultHeaderStyle().setBorder(Border.PEN_1_POINT());
		return dynamicReport;
	}

	protected LayoutManager getLayoutManager() {
		return new ClassicLayoutManager();
	}

	protected JRDataSource getDataSource(DynamicReport dr,Collection<?> dummyCollection) {
		dummyCollection = SortUtils.sortCollection(dummyCollection, dr.getColumns());
		JRDataSource ds = new JRBeanCollectionDataSource(dummyCollection);
		return ds;
	}

	public static void exportReportXls(JasperPrint jp, String path, SimpleXlsReportConfiguration configuration)
			throws JRException, FileNotFoundException {
		JRXlsExporter exporter = new JRXlsExporter();

		File outputFile = new File(path);
		File parentFile = outputFile.getParentFile();
		if (parentFile != null)
			parentFile.mkdirs();
		FileOutputStream fos = new FileOutputStream(outputFile);

		exporter.setConfiguration(configuration);

		SimpleExporterInput simpleExporterInput = new SimpleExporterInput(jp);
		OutputStreamExporterOutput simpleOutputStreamExporterOutput = new SimpleOutputStreamExporterOutput(fos);

		exporter.setExporterInput(simpleExporterInput);
		exporter.setExporterOutput(simpleOutputStreamExporterOutput);

		exporter.exportReport();

		log.debug("Xlsx Report exported: " + path);
	}

	public static void exportReport(JasperPrint jp, String path) throws JRException, FileNotFoundException {
		log.debug("Exporing report to: " + path);
		JRPdfExporter exporter = new JRPdfExporter();
		File outputFile = new File(path);
		File parentFile = outputFile.getParentFile();
		if (parentFile != null)
			parentFile.mkdirs();
		FileOutputStream fos = new FileOutputStream(outputFile);
		SimpleExporterInput simpleExporterInput = new SimpleExporterInput(jp);
		OutputStreamExporterOutput simpleOutputStreamExporterOutput = new SimpleOutputStreamExporterOutput(fos);
		exporter.setExporterInput(simpleExporterInput);
		exporter.setExporterOutput(simpleOutputStreamExporterOutput);
		exporter.exportReport();
		log.debug("Report exported: " + path);
	}

	protected void exportReport(DynamicReport dr) throws Exception {
		exportReport(jp,System.getProperty("user.dir") + "/target/reports/" + this.getClass().getSimpleName() + ".pdf");
		exportToJRXML(dr);
	}

	protected void exportToJRXML(DynamicReport dr) throws Exception {
		if (this.jr != null) {
			DynamicJasperHelper.generateJRXML(this.jr, "UTF-8",
					System.getProperty("user.dir") + "/target/reports/" + this.getClass().getSimpleName() + ".jrxml");

		} else {
			DynamicJasperHelper.generateJRXML(dr, this.getLayoutManager(), this.params, "UTF-8",
					System.getProperty("user.dir") + "/target/reports/" + this.getClass().getSimpleName() + ".jrxml");
		}
	}

	public static void main(String[] args) throws Exception {
		
		
		Main run = new Main();
		// récupération des données et paramètre
		run.getParam("Trimestre3",getTrimestre(), 3, "test des trimestres", "exemple");
		// initialisation de dynamic reporting
		DynamicReport dr = run.buildReportTemplate();
		JRDataSource ds = run.getDataSource(dr,getDummyCollection());
		// constructure du template
		run.construireReport(ds, dr);
		// g"n"ration des template
		run.exportReport(dr);
	}

}
