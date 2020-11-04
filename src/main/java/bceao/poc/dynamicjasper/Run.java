//package bceao.poc.dynamicjasper;
//
//import java.util.Collection;
//
//import ar.com.fdvs.dj.core.DynamicJasperHelper;
//import ar.com.fdvs.dj.domain.DynamicReport;
//import ar.com.fdvs.dj.domain.Style;
//import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
//import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
//import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
//import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
//import ar.com.fdvs.dj.util.SortUtils;
//import net.sf.jasperreports.engine.JRDataSource;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//
//public class Run extends BaseDjReport {
//
//    public DynamicReport buildReportTemplate() throws Exception {
//
//	Style detailStyle = new Style();
//	Style headerStyle = new Style();
//
//	Style titleStyle = new Style();
//	Style subtitleStyle = new Style();
//	Style amountStyle = new Style();
//	amountStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
//
//	/*
//	 * Creates the DynamicReportBuilder and sets the basic options for the report
//	 */
//	DynamicReportBuilder drb = new DynamicReportBuilder();
//	drb.setTitle("November " + getYear() + " sales report") // defines the title of the report
//		.setSubtitle("The items in this report correspond "
//			+ "to the main products: DVDs, Books, Foods and Magazines")
//		.setDetailHeight(15) // defines the height for each record of the report
//		.setMargins(30, 20, 30, 15) // define the margin space for each side (top, bottom, left and right)
//		.setDefaultStyles(titleStyle, subtitleStyle, headerStyle, detailStyle).setColumnsPerPage(1); // defines
//													     // columns
//													     // per page
//													     // (like in
//													     // the
//													     // telephone
//													     // guide)
//
//	/*
//	 * Note that we still didn�t call the build() method
//	 */
//
//	/*
//	 * Column definitions. We use a new ColumnBuilder instance for each column, the
//	 * ColumnBuilder.getNew() method returns a new instance of the builder
//	 */
//	AbstractColumn columnState = ColumnBuilder.getNew() // creates a new instance of a ColumnBuilder
//		.setColumnProperty("state", String.class.getName()) // defines the field of the data source that this
//								    // column will show, also its type
//		.setTitle("State") // the title for the column
//		.setWidth(85) // the width of the column
//		.build(); // builds and return a new AbstractColumn
//
//	// Create more columns
//	AbstractColumn columnBranch = ColumnBuilder.getNew().setColumnProperty("branch", String.class.getName())
//		.setTitle("Branch").setWidth(85).build();
//
//	AbstractColumn columnaProductLine = ColumnBuilder.getNew()
//		.setColumnProperty("productLine", String.class.getName()).setTitle("Product Line").setWidth(85).build();
//
//	AbstractColumn columnaItem = ColumnBuilder.getNew().setColumnProperty("item", String.class.getName())
//		.setTitle("Item").setWidth(85).build();
//
//	AbstractColumn columnCode = ColumnBuilder.getNew().setColumnProperty("id", Long.class.getName()).setTitle("ID")
//		.setWidth(40).build();
//
//	AbstractColumn columnaCantidad = ColumnBuilder.getNew().setColumnProperty("quantity", Long.class.getName())
//		.setTitle("Quantity").setWidth(80).build();
//
//	AbstractColumn columnAmount = ColumnBuilder.getNew().setColumnProperty("amount", Float.class.getName())
//		.setTitle("Amount").setWidth(90).setPattern("$ 0.00") // defines a pattern to apply to the values swhown
//								      // (uses TextFormat)
//		.setStyle(amountStyle) // special style for this column (align right)
//		.build();
//
//	/*
//	 * We add the columns to the report (through the builder) in the order we want
//	 * them to appear
//	 */
//	drb.addColumn(columnState);
//	drb.addColumn(columnBranch);
//	drb.addColumn(columnaProductLine);
//	drb.addColumn(columnaItem);
//	drb.addColumn(columnCode);
//	drb.addColumn(columnaCantidad);
//	drb.addColumn(columnAmount);
//
//	/*
//	 * add some more options to the report (through the builder)
//	 */
//	drb.setUseFullPageWidth(true); // we tell the report to use the full width of the page. this rezises
//				       // the columns width proportionally to meat the page width.
//
//
//	DynamicReport dr = drb.build(); // Finally build the report!
//
//	return dr;
//    }
//
//    public void construireReport(JRDataSource ds, DynamicReport dr) throws Exception {
//
//
//	/*
//	 * Get a JRDataSource implementation
//	 */
//
//
//	/*
//	 * Creates the JasperReport object, we pass as a Parameter the DynamicReport, a
//	 * new ClassicLayoutManager instance (this one does the magic) and the
//	 * JRDataSource
//	 */
//	jr = DynamicJasperHelper.generateJasperReport(dr, getLayoutManager(), params);
//
//	/*
//	 * Creates the JasperPrint object, we pass as a Parameter the JasperReport
//	 * object, and the JRDataSource
//	 */
//	log.debug("Filling the report");
//	if (ds != null)
//	    jp = JasperFillManager.fillReport(jr, params, ds);
//	else
//	    jp = JasperFillManager.fillReport(jr, params);
//
//	log.debug("Filling done!");
//	log.debug("Exporting the report (pdf, xls, etc)");
//
//
//	log.debug("test finished");
//    }
//
//    protected JRDataSource getDataSource(DynamicReport dr) {
//	Collection dummyCollection = TestRepositoryProducts.getDummyCollection();
//	dummyCollection = SortUtils.sortCollection(dummyCollection, dr.getColumns());
//
//	// here contains dummy hardcoded objects...
//	return new JRBeanCollectionDataSource(dummyCollection);
//    }
//    
//    public static void main(String[] args) throws Exception {
//	Run test = new Run();
//
//	DynamicReport dr = test.buildReportTemplate();
//
//	JRDataSource ds = test.getDataSource(dr);
//
//
//
//	test.construireReport(ds, dr);
//
//	test.exportReport(dr);
//    }
//
//}