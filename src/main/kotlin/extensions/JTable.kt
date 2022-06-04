package extensions

import javax.swing.JTable

fun JTable.selectedFirstColumnOfRaw(): Any {
    return getValueAt(selectedRow, 0)
}