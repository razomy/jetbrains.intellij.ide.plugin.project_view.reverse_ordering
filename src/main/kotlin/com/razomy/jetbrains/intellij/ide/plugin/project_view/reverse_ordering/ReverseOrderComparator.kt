// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.razomy.jetbrains.intellij.ide.plugin.project_view.reverse_ordering

import com.intellij.ide.projectView.NodeSortKey
import com.intellij.ide.projectView.NodeSortSettings
import com.intellij.ide.projectView.ProjectView
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.impl.GroupByTypeComparator
import com.intellij.ide.util.treeView.AlphaComparator
import com.intellij.ide.util.treeView.NodeDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil

fun reverse_order_string_compare(a_string: String, b_string: String): Int {
    val a_key = a_string.split(Regex("[ \\-._]|(?=[A-Z])")).reversed()
    val b_key = b_string.split(Regex("[ \\-._]|(?=[A-Z])")).reversed()
    for (i in 0 until minOf(a_key.size, b_key.size)) {
        val a_i = a_key[i]
        val b_i = b_key[i]
        val comparison = StringUtil.naturalCompare(a_i, b_i)
        if (comparison != 0) return comparison;
    }
    return a_string.length - b_string.length
}

fun reverse_order_project_view_compare(descriptor1: ProjectViewNode<*>, descriptor2: ProjectViewNode<*>): Int {
    val key1 = descriptor1.name;
    val key2 = descriptor2.name;
    if (key1 == null && key2 == null) return 0
    if (key1 == null) return -1
    if (key2 == null) return 1

    return reverse_order_string_compare(key1, key2)
}


open class ReverseOrderComparator : Comparator<NodeDescriptor<*>> {
    private val project: Project?
    private var myPaneId: String? = null
    private var myForceSortByType = false

    constructor(project: Project?, paneId: String?) {
        this.project = project
        myPaneId = paneId
    }

    constructor(forceSortByType: Boolean) {
        myForceSortByType = forceSortByType
        this.project = null
    }

    override fun compare(descriptor1: NodeDescriptor<*>?, descriptor2: NodeDescriptor<*>?): Int {
        var descriptor1 = descriptor1
        var descriptor2 = descriptor2
        descriptor1 = getUpdatedDescriptor(descriptor1!!)
        descriptor2 = getUpdatedDescriptor(descriptor2!!)

        if (descriptor1 is ProjectViewNode<*> && descriptor2 is ProjectViewNode<*>) {
            val settings = NodeSortSettings.of(
                isManualOrder,
                sortKey,
                isFoldersAlwaysOnTop
            )
            val sortResult = descriptor1.getSortOrder(settings).compareTo(descriptor2.getSortOrder(settings))
            if (sortResult != 0) return sortResult

            if (settings.isManualOrder) {
                val key1 = descriptor1.manualOrderKey
                val key2 = descriptor2.manualOrderKey
                val result = compare(key1, key2)
                if (result != 0) return result
            }

            if (settings.isFoldersAlwaysOnTop) {
                val typeWeight1 = descriptor1.getTypeSortWeight(settings.isSortByType)
                val typeWeight2 = descriptor2.getTypeSortWeight(settings.isSortByType)
                if (typeWeight1 != 0 && typeWeight2 == 0) {
                    return -1
                }
                if (typeWeight1 == 0 && typeWeight2 != 0) {
                    return 1
                }
                if (typeWeight1 != 0 && typeWeight2 != typeWeight1) {
                    return typeWeight1 - typeWeight2
                }
            }

            when (settings.sortKey) {
                NodeSortKey.BY_NAME -> {
                    val sortKey1 = descriptor1.sortKey
                    val sortKey2 = descriptor2.sortKey
                    if (sortKey1 != null && sortKey2 != null) {
                        val result = compare(sortKey1, sortKey2)
                        if (result != 0) return result
                    }
                }

                NodeSortKey.BY_TYPE -> {
                    val result = reverse_order_project_view_compare(descriptor1, descriptor2)
                    if (result != 0) return  result
                }

                NodeSortKey.BY_TIME_DESCENDING, NodeSortKey.BY_TIME_ASCENDING -> {
                    val timeSortKey1 = descriptor1.timeSortKey
                    val timeSortKey2 = descriptor2.timeSortKey
                    val result: Int = if (settings.sortKey == NodeSortKey.BY_TIME_ASCENDING)
                        compare(timeSortKey1, timeSortKey2, false)
                    else
                        compare(timeSortKey2, timeSortKey1, true)
                    if (result != 0) return result
                }
            }

            if (isAbbreviateQualifiedNames) {
                val key1 = descriptor1.qualifiedNameSortKey
                val key2 = descriptor2.qualifiedNameSortKey
                if (key1 != null && key2 != null) {
                    return StringUtil.naturalCompare(key1, key2)
                }
            }
        }
        if (descriptor1 == null) return -1
        if (descriptor2 == null) return 1
        return AlphaComparator.INSTANCE.compare(descriptor1, descriptor2)
    }

    private fun getUpdatedDescriptor(descriptor: NodeDescriptor<*>): NodeDescriptor<*> {
        var descriptor = descriptor
        if (sortKey == NodeSortKey.BY_NAME &&
            descriptor is ProjectViewNode<*> && descriptor.isSortByFirstChild
        ) {
            val children = descriptor.children
            if (!children.isEmpty()) {
                descriptor = children.iterator().next()!!
                descriptor.update()
            }
        }
        return descriptor
    }

    protected val sortKey: NodeSortKey
        get() {
            if (project == null) {
                return if (myForceSortByType) NodeSortKey.BY_TYPE else NodeSortKey.BY_NAME
            }
            return ProjectView.getInstance(project).getSortKey(myPaneId)
        }

    protected val isManualOrder: Boolean
        get() {
            if (project == null) {
                return true
            }
            return ProjectView.getInstance(project).isManualOrder(myPaneId)
        }

    protected val isAbbreviateQualifiedNames: Boolean
        get() = project != null && ProjectView.getInstance(project).isAbbreviatePackageNames(myPaneId)

    protected val isFoldersAlwaysOnTop: Boolean
        get() = project == null || ProjectView.getInstance(project).isFoldersAlwaysOnTop(myPaneId)

    companion object {
        private fun compare(key1: Comparable<*>?, key2: Comparable<*>?, nullsLast: Boolean = true): Int {
            if (key1 == null && key2 == null) return 0
            if (key1 == null) return if (nullsLast) 1 else -1
            if (key2 == null) return if (nullsLast) -1 else 1
            if (key1 is String && key2 is String) {
                return StringUtil.naturalCompare(key1, key2)
            }

            return try {
                key1!!.compareTo(key2 as Nothing)
            } catch (ignored: ClassCastException) {
                // if custom nodes provide comparable keys of different types,
                // let's try to compare class names instead to avoid broken trees
                key1.javaClass.name.compareTo(key2.javaClass.name)
            }
        }
    }
}