package com.razomy.jetbrains.intellij.ide.plugin.project_view.reverse_ordering

import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.util.treeView.NodeDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import java.io.File

class ConfigBasedComparator(private val project: Project?, private val paneId: String?) : Comparator<NodeDescriptor<*>> {
    private fun loadConfigOrder(directory: String): List<String> {
        val configFile = File(directory, "config.order.rn")
        return if (configFile.exists()) {
            configFile.readLines().map { it.trim() }
        } else {
            emptyList()
        }
    }

    private fun getSortIndex(fileName: String, directory: String): Int {
        val configOrder = loadConfigOrder(directory)
        return configOrder.indexOf(fileName).takeIf { it >= 0 } ?: Int.MAX_VALUE
    }

    override fun compare(descriptor1: NodeDescriptor<*>?, descriptor2: NodeDescriptor<*>?): Int {
        descriptor1 ?: return -1
        descriptor2 ?: return 1

        val node1 = descriptor1 as? ProjectViewNode<*>
        val node2 = descriptor2 as? ProjectViewNode<*>
        val name1 = node1?.name ?: return -1
        val name2 = node2?.name ?: return 1
        val directory1 = node1?.virtualFile?.parent?.path ?: ""
        val directory2 = node2?.virtualFile?.parent?.path ?: ""

        val index1 = getSortIndex(name1, directory1)
        val index2 = getSortIndex(name2, directory2)

        return index1.compareTo(index2).takeIf { it != 0 } ?: StringUtil.naturalCompare(name1, name2)
    }
}
