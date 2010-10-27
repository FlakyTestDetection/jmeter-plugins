package kg.apc.jmeter.vizualizers;

import kg.apc.jmeter.charting.AbstractGraphRow;
import kg.apc.jmeter.charting.GraphRowSumValues;
import org.apache.jmeter.samplers.SampleResult;

/**
 *
 * @author Stephane hoblingre
 */
public class TransactionsPerSecondGui
        extends AbstractGraphPanelVisualizer
{
    //private static final Logger log = LoggingManager.getLoggerForClass();

    /**
     *
     */
    public TransactionsPerSecondGui()
    {
        super();
        graphPanel.getGraphObject().setxAxisLabelRenderer(new DateTimeRenderer(
                "HH:mm:ss"));
        graphPanel.getGraphObject().setDrawFinalZeroingLines(false);
        delay = 1000;
    }

    private synchronized AbstractGraphRow getNewRow(String label)
    {
        AbstractGraphRow row = null;
        if (!model.containsKey(label))
        {
            row = new GraphRowSumValues(false);
            row.setLabel(label);
            row.setColor(colors.getNextColor());
            row.setDrawLine(true);
            row.setMarkerSize(AbstractGraphRow.MARKER_SIZE_SMALL);
            model.put(label, row);
            graphPanel.addRow(row);
        } else
        {
            row = model.get(label);
        }

        return row;
    }

    private void addOneTransaction(String threadGroupName, long time)
    {
        AbstractGraphRow row = model.get(threadGroupName);
        if (row == null)
        {
            row = getNewRow(threadGroupName);
        }

        row.add(time, 1);
    }

    public String getLabelResource()
    {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getStaticLabel()
    {
        return "Transactions per Second";
    }

    public void add(SampleResult res)
    {
        addOneTransaction(res.getSampleLabel(), res.getEndTime() - res.getEndTime() % delay);
        updateGui(null);
    }
}