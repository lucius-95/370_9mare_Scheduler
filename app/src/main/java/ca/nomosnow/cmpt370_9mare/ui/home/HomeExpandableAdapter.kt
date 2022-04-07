package ca.nomosnow.cmpt370_9mare.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import ca.nomosnow.cmpt370_9mare.databinding.EventItemBinding
import ca.nomosnow.cmpt370_9mare.databinding.GroupEventBinding

class HomeExpandableAdapter internal constructor(
    private val context: Context,
    private val titleList: List<String>,
    private val dataList: HashMap<String, List<String>>
) : BaseExpandableListAdapter() {

    // Ensure bindings and inflater are within the current context
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private lateinit var groupBinding: GroupEventBinding
    private lateinit var itemBinding: EventItemBinding

    /**
     * GET functionalities
     */

    // Get size
    override fun getGroupCount(): Int {
        return this.titleList.size
    }

    // Get children size
    override fun getChildrenCount(listPosition: Int): Int {
        return this.dataList[this.titleList[listPosition]]!!.size
    }

    // Get all in group
    override fun getGroup(listPosition: Int): Any {
        return this.titleList[listPosition]
    }

    // Get specific child
    override fun getChild(listPosition: Int, expandableListPosition: Int): Any {
        return this.dataList[this.titleList[listPosition]]!![expandableListPosition]
    }

    // Get group ID
    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    // Get child ID
    override fun getChildId(listPosition: Int, expandableListPosition: Int): Long {
        return expandableListPosition.toLong()
    }

    // Ensure stable ID is false
    override fun hasStableIds(): Boolean {
        return false
    }

    // Get entire group as a view
    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        view: View?,
        parent: ViewGroup?
    ): View {
        var convertView = view
        val holder: GroupViewHolder
        if (convertView == null) {
            groupBinding = GroupEventBinding.inflate(inflater, parent, false)
            convertView = groupBinding.root
            holder = GroupViewHolder()
            holder.label = groupBinding.listEvent
            convertView.tag = holder
        } else {
            holder = convertView.tag as GroupViewHolder
        }
        val listTitle = getGroup(listPosition) as String
        holder.label!!.text = listTitle
        return convertView

    }

    // Get all children as a view
    override fun getChildView(
        listPosition: Int,
        expandableListPosition: Int,
        isLastChild: Boolean,
        view: View?,
        parent: ViewGroup?
    ): View {
        var convertView = view
        val holder: ItemViewHolder
        if (convertView == null) {
            itemBinding = EventItemBinding.inflate(inflater, parent, false)
            convertView = itemBinding.root
            holder = ItemViewHolder()
            holder.label = itemBinding.expandedListItem
            convertView.tag = holder
        } else {
            holder = convertView.tag as ItemViewHolder
        }

        val expandableListText = getChild(listPosition, expandableListPosition) as String
        holder.label!!.text = expandableListText
        return convertView
    }

    // Make sure all children are selectable
    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }

    // Labeling for item and group view holders
    inner class ItemViewHolder {
        internal var label: TextView? = null
    }

    inner class GroupViewHolder {
        internal var label: TextView? = null
    }


}