package io.framed.view

import org.w3c.dom.HTMLTableCellElement

/**
 * @author lars
 */
class TableCell : ViewCollection<View<*>, HTMLTableCellElement>("td") {
}

fun TableRow.cell(init: TableCell.() -> Unit): TableCell {
    val view = TableCell()
    append(view)
    init(view)
    return view
}

fun TableCell.listView(init: ListView.() -> Unit): ListView {
    val view = ListView()
    append(view)
    init(view)
    return view
}

fun TableRow.cellBox(init: ListView.() -> Unit): ListView {
    val view = ListView()
    cell {
        append(view)
    }
    init(view)
    return view
}