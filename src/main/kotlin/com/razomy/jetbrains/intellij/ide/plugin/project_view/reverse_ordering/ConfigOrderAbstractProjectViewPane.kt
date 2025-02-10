package com.razomy.jetbrains.intellij.ide.plugin.project_view.reverse_ordering

import com.intellij.ide.SelectInTarget
import com.intellij.ide.projectView.impl.ProjectAbstractTreeStructureBase
import com.intellij.ide.projectView.impl.ProjectViewPane
import com.intellij.ide.util.treeView.NodeDescriptor
import com.intellij.openapi.project.Project


class ConfigOrderAbstractProjectViewPane(project: Project) : ProjectViewPane(project) {
    var ID = "ConfigOrderProject"
    override fun getTitle(): String {
        return ID;
    }

    // should be first
    override fun getWeight(): Int {
        return -1
    }

    override fun getId(): String {
        return ID;
    }

    override fun createComparator(): java.util.Comparator<NodeDescriptor<*>> {
        return ConfigBasedComparator(myProject, ID)
    }

    override fun createSelectInTarget(): SelectInTarget {
        return ProjectPaneSelectInTarget(myProject, ID)
    }

    override fun createStructure(): ProjectAbstractTreeStructureBase {
        return ProjectViewPaneTreeStructure(this.myProject, ID);
    }
}