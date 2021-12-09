package GUI;

import burp.*;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class Tags extends AbstractTableModel implements ITab, IMessageEditorController {
    private IBurpExtenderCallbacks callbacks;
    private IExtensionHelpers helpers;
    private String tagName;
    private JSplitPane mjSplitPane;
    private List<Tags.TablesData> Udatas = new ArrayList();
    private IMessageEditor HRequestTextEditor;
    private IMessageEditor HResponseTextEditor;
    private IHttpRequestResponse currentlyDisplayedItem;
    private Tags.URLTable Utable;
    private JScrollPane UscrollPane;
    private JSplitPane HjSplitPane;
    private JTabbedPane Ltable;
    private JTabbedPane Rtable;

    public Tags(IBurpExtenderCallbacks callbacks, String name) {
        this.callbacks = callbacks;
        this.helpers = callbacks.getHelpers();
        this.tagName = name;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Tags.this.mjSplitPane = new JSplitPane(0);
                Tags.this.Utable = Tags.this.new URLTable(Tags.this);
                Tags.this.UscrollPane = new JScrollPane(Tags.this.Utable);
                Tags.this.HjSplitPane = new JSplitPane();
                Tags.this.HjSplitPane.setDividerLocation(0.5D);
                Tags.this.Ltable = new JTabbedPane();
                Tags.this.HRequestTextEditor = Tags.this.callbacks.createMessageEditor(Tags.this, false);
                Tags.this.Ltable.addTab("Request", Tags.this.HRequestTextEditor.getComponent());
                Tags.this.Rtable = new JTabbedPane();
                Tags.this.HResponseTextEditor = Tags.this.callbacks.createMessageEditor(Tags.this, false);
                Tags.this.Rtable.addTab("Response", Tags.this.HResponseTextEditor.getComponent());
                Tags.this.HjSplitPane.add(Tags.this.Ltable, "left");
                Tags.this.HjSplitPane.add(Tags.this.Rtable, "right");
                Tags.this.mjSplitPane.add(Tags.this.UscrollPane, "left");
                Tags.this.mjSplitPane.add(Tags.this.HjSplitPane, "right");
                Tags.this.callbacks.customizeUiComponent(Tags.this.mjSplitPane);
                Tags.this.callbacks.addSuiteTab(Tags.this);
            }
        });
    }

    public String getTabCaption() {
        return this.tagName;
    }

    public Component getUiComponent() {
        return this.mjSplitPane;
    }

    public int getRowCount() {
        return this.Udatas.size();
    }

    public int getColumnCount() {
        return 6;
    }

    public String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return "id";
            case 1:
                return "url";
            case 2:
                return "statusCode";
            case 3:
                return "issue";
            case 4:
                return "startTime";
            case 5:
                return "length";
            default:
                return null;
        }
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Tags.TablesData datas = (Tags.TablesData)this.Udatas.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return datas.id;
            case 1:
                return datas.url;
            case 2:
                return datas.statusCode;
            case 3:
                return datas.issue;
            case 4:
                return datas.startTime;
            case 5:
                return datas.length;
            default:
                return null;
        }
    }

    public byte[] getRequest() {
        return this.currentlyDisplayedItem.getRequest();
    }

    public byte[] getResponse() {
        return this.currentlyDisplayedItem.getResponse();
    }

    public IHttpService getHttpService() {
        return this.currentlyDisplayedItem.getHttpService();
    }

    public int add(String url, int statusCode, String issue, IHttpRequestResponse requestResponse,int length) {
        synchronized(this.Udatas) {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTime = sdf.format(d);
            int id = this.Udatas.size();
            this.Udatas.add(new Tags.TablesData(id, url, statusCode, issue, requestResponse, startTime, length));
            this.fireTableRowsInserted(id, id);
            return id;
        }
    }

    public int save(int id, String url, int statusCode, String issue, IHttpRequestResponse requestResponse,int length) {
        Tags.TablesData dataEntry = (Tags.TablesData)this.Udatas.get(id);
        String startTime = dataEntry.startTime;
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endTime = sdf.format(d);
        synchronized(this.Udatas) {
            this.Udatas.set(id, new Tags.TablesData(id,  url, statusCode, issue, requestResponse, startTime, length));
            this.fireTableRowsUpdated(id, id);
            return id;
        }
    }

    public static class TablesData {
        final int id;
        final String url;
        final int statusCode;
        final String issue;
        final IHttpRequestResponse requestResponse;
        final String startTime;
        final int length;

        public TablesData(int id,  String url, int statusCode, String issue, IHttpRequestResponse requestResponse, String startTime, int length) {
            this.id = id;
            this.url = url;
            this.statusCode = statusCode;
            this.issue = issue;
            this.requestResponse = requestResponse;
            this.startTime = startTime;
            this.length = length;
        }
    }

    public class URLTable extends JTable {
        public URLTable(TableModel tableModel) {
            super(tableModel);
        }

        public void changeSelection(int row, int col, boolean toggle, boolean extend) {
            Tags.TablesData dataEntry = (Tags.TablesData)Tags.this.Udatas.get(this.convertRowIndexToModel(row));
            Tags.this.HRequestTextEditor.setMessage(dataEntry.requestResponse.getRequest(), true);
            Tags.this.HResponseTextEditor.setMessage(dataEntry.requestResponse.getResponse(), false);
            Tags.this.currentlyDisplayedItem = dataEntry.requestResponse;
            super.changeSelection(row, col, toggle, extend);
        }
    }
}

