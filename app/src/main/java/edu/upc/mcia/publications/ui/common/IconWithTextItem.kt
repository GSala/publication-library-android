package edu.upc.mcia.publications.ui.common

import edu.upc.mcia.publications.App
import edu.upc.mcia.publications.R
import edu.upc.mcia.publications.ui.base.BaseItem

class IconWithTextItem(iconRes: Int,
                       val primaryText: String,
                       val secondaryText: String) : BaseItem {

    val icon = App.getContext().getDrawable(iconRes)

    override val layoutResource: Int = R.layout.item_icon_with_text
}