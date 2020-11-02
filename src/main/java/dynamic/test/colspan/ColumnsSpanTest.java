package dynamic.test.colspan;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import dynamic.test.BaseDjReportTest;
import net.sf.jasperreports.view.JasperViewer;

public class ColumnsSpanTest extends BaseDjReportTest {

	/**
	 * Creates the DynamicReportBuilder and sets the basic options for the
	 * report
	 */

	FastReportBuilder frb = new FastReportBuilder();

	public List<String> getTrimestre() {
		List<String> lt = new ArrayList<String>();
		lt.add("Trimestre1");
		lt.add("Trimestre2");
		lt.add("Trimestre3");
		lt.add("Trimestre4");
		lt.add("Trimestre5");
		lt.add("Trimestre6");
		return lt;
	}

	public void getParam(String trimestreDepart, int nbrTrimestre, String titre, String sousTitre) throws Exception {
		int j = 1;
		List<String> lt = getTrimestre();

		frb.addColumn("Pays", "state", String.class.getName(), 30);
		frb.setTitle(titre + getYear());
		frb.setSubtitle(sousTitre + " :" + new Date());
		frb.setColumnsPerPage(1, 10);
		frb.setUseFullPageWidth(true);

		for (int i = 0; i < nbrTrimestre; i++) {
			frb.addColumn("item1", "amount", Float.class.getName(), 30);
			frb.addColumn("item2", "branch", String.class.getName(), 30);
			frb.addColumn("item3", "item", String.class.getName(), 30);
		}

		for (String string : lt) {
			if (lt.indexOf(trimestreDepart) <= lt.indexOf(string) && (lt.indexOf(string)) <= nbrTrimestre + 1) {
				//System.out.println("lt.indexOf(trimestre)" + lt.indexOf(trimestreDepart));
				//System.out.println("lt.indexOf(string)" + lt.indexOf(string));
				frb.setColspan(j, 3, string);
				j = j + 3;
			}
		}
	}

	public DynamicReport buildReport() throws Exception {
		// int nbrTrimestre=3;
		// FastReportBuilder frb = new FastReportBuilder();
		//
		// frb.addColumn("Pays", "state", String.class.getName(), 30);
		// frb.setTitle("BCEAO " + getYear() +" donnée report");
		// frb.setSubtitle("This report was generated at " + new Date());
		// frb.setColumnsPerPage(1, 10);
		// frb.setUseFullPageWidth(true);
		// int j=1;
		// for(int i=0;i<nbrTrimestre;i++){
		// frb.addColumn("item1", "amount", Float.class.getName(), 30);
		// frb.addColumn("item2", "branch", String.class.getName(), 30);
		// frb.addColumn("item3", "item", String.class.getName(), 30);
		// }
		// for(int i=0;i<nbrTrimestre;i++){
		// frb.setColspan(j, 3, "Trimestre "+j);
		// j=j+3;
		// }
		getParam("Trimestre3", 4, "test des trimestres", "exemple");
		DynamicReport dynamicReport = frb.build();

		dynamicReport.getOptions().getDefaultHeaderStyle().setBorder(Border.PEN_1_POINT());

		return dynamicReport;

	}

	// public DynamicReport buildReport() throws Exception {
	//
	// FastReportBuilder frb = new FastReportBuilder();
	//
	// frb.addColumn("Pays", "state", String.class.getName(), 30);
	// frb.addColumn("item1", "branch", String.class.getName(), 30);
	// frb.addColumn("Item2", "item", String.class.getName(), 50);
	// frb.addColumn("Item1", "amount", Float.class.getName(), 60, true);
	// frb.addColumn("item2", "id", Long.class.getName(), 10);
	// frb.addColumn("item3", "quantity", Long.class.getName(), 30);
	// frb.setTitle("November " + getYear() +" sales report");
	// frb.setSubtitle("This report was generated at " + new Date());
	// frb.setColumnsPerPage(1, 10);
	// frb.setUseFullPageWidth(true);
	// frb.setColspan(1, 2, "Trimestre 1");
	// frb.setColspan(3, 3, "Trimestre 2");
	//
	// DynamicReport dynamicReport = frb.build();
	//
	// dynamicReport.getOptions().getDefaultHeaderStyle().setBorder(Border.PEN_1_POINT());
	//
	// return dynamicReport;
	// }

	
	public static void main(String[] args) throws Exception {

		
		ColumnsSpanTest test = new ColumnsSpanTest();
		test.testReport();
		JasperViewer.viewReport(test.jp);
	}

}
