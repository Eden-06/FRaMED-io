package io.framed.view

import org.w3c.dom.HTMLTableElement

/**
 * @author lars
 */
class TableView : ViewCollection<TableRow, HTMLTableElement>("table") {

}

fun ListView.tableView(init: TableView.() -> Unit): TableView {
    val view = TableView()
    append(view)
    init(view)
    return view
}