package org.compiere.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

public class MerpssampleSchedule extends X_erps_sampleSchedule {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3556889018983860250L;
	
	/**	Sample Lines	*/
	private MerpssampleScheduleLine[] s_lines;

	public MerpssampleSchedule(Properties ctx, int erps_sampleSchedule_ID,
			String trxName) {
		super(ctx, erps_sampleSchedule_ID, trxName);
	}

	public MerpssampleSchedule(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * 	Get Sample Lines of Sample
	 * 	@param whereClause starting with AND
	 * 	@return lines
	 */
	private MerpssampleScheduleLine[] getLinesSample (String whereClause)
	{
		String whereClauseFinal = "erps_sampleschedule_ID=? ";
		if (whereClause != null)
			whereClauseFinal += whereClause;
		List<MerpssampleScheduleLine> list = new Query(getCtx(), I_erps_sampleScheduleLine.Table_Name, whereClauseFinal, get_TrxName())
										.setParameters(geterps_sampleSchedule_ID())
										.setOrderBy(I_erps_sampleScheduleLine.COLUMNNAME_erps_sampleScheduleLine_ID)
										.list();
		return list.toArray(new MerpssampleScheduleLine[list.size()]);
	}	//	getLinesSample

	/**
	 * 	Get Sample Lines
	 * 	@param requery
	 * 	@return lines
	 */
	public MerpssampleScheduleLine[] getLinesSample (boolean requery)
	{
		if (s_lines == null || s_lines.length == 0 || requery)
			s_lines = getLinesSample(null);
		set_TrxName(s_lines, get_TrxName());
		return s_lines;
	}	//	getLinesSample

	/**
	 * 	Get Lines of Sample
	 * 	@return lines
	 */
	public MerpssampleScheduleLine[] getLinesSample()
	{
		return getLinesSample(false);
	}	//	getLinesSample

}
