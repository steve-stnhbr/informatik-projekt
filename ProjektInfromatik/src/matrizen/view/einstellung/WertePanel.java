package matrizen.view.einstellung;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.TableUI;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import matrizen.core.DateiManager;

public class WertePanel extends JPanel implements Benennbar {
	private static final long serialVersionUID = -5240922060073322637L;
	private static WertePanel instanz;

	private JTable table;
	private JScrollPane scroll;
	private JButton bAbbruch, bWeiter;

	private WertePanel() {
		initComponents();
		initListeners();
		addComponents();

		setPreferredSize(new Dimension(509, 515));

		setVisible(true);
	}

	private void initComponents() {
		table = new JTable(new WerteTableModel());
		scroll = new JScrollPane(table);
		bAbbruch = new JButton(AnfangsFenster.textAbbruch);
		bWeiter = new JButton(AnfangsFenster.textWeiter);
	}

	private void initListeners() {
		bAbbruch.addActionListener((e) -> {
			AnfangsFenster.gibInstanz().inhaltAendern(EinstellungsPanel.gibInstanz());
			instanz = null;
		});

		bWeiter.addActionListener((e) -> {
			AnfangsFenster.gibInstanz().inhaltAendern(EinstellungsPanel.gibInstanz());
			DateiManager.werte.setMap(((WerteTableModel) table.getModel()).mapErzeugen());
		});
	}

	private void addComponents() {
		add(scroll);
		add(bAbbruch);
		add(bWeiter);
	}

	@Override
	public String gibName() {
		return "Werte ändern";
	}

	static public WertePanel gibInstanz() {
		if (instanz == null)
			instanz = new WertePanel();
		return instanz;
	}

	class WerteTableModel implements TableModel {
		private HashMap<String, Integer> map;
		private ArrayList<String> keys;
		private ArrayList<Integer> values;

		WerteTableModel() {
			map = DateiManager.werte.getMap();
			keys = new ArrayList<>(map.keySet());

			keys.sort(new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
			});

			values = new ArrayList<>();

			for (String s : keys) {
				values.add(map.get(s));
			}
		}

		public HashMap<String, Integer> mapErzeugen() {
			HashMap<String, Integer> m = new HashMap<String, Integer>();

			for (int i = 0; i < values.size(); i++)
				m.put(keys.get(i), values.get(i));

			return m;
		}

		@Override
		public int getRowCount() {
			return map.size();
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0)
				return "Name:";
			else if (columnIndex == 1)
				return "Wert:";
			else
				return null;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 0)
				return String.class;
			else if (columnIndex == 1)
				return Integer.class;
			else
				return null;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == 1;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (columnIndex == 0)
				return "\"" + keys.get(rowIndex) + "\"";
			else if (columnIndex == 1)
				return values.get(rowIndex);
			return null;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			if (columnIndex == 0)
				keys.set(rowIndex, (String) aValue);
			else if (columnIndex == 1) {
				if (aValue instanceof Integer || (aValue instanceof String && new Integer((String) aValue) != null))
					values.set(rowIndex, (Integer) aValue);
			}
		}

		@Override
		public void addTableModelListener(TableModelListener l) {
		}

		@Override
		public void removeTableModelListener(TableModelListener l) {
		}

	}

}
