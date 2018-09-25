package io.framed.view

import org.w3c.dom.HTMLTableRowElement

/**
 * @author lars
 */
class TableRow: ViewCollection<TableCell, HTMLTableRowElement>("tr") {
}

fun TableView.row(init: TableRow.() -> Unit): TableRow {
    val view = TableRow()
    append(view)
    init(view)
    return view
}