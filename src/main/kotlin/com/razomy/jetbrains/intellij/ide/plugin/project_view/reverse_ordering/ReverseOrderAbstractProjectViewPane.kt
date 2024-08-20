package com.razomy.jetbrains.intellij.ide.plugin.project_view.reverse_ordering

import com.intellij.ide.SelectInContext
import com.intellij.ide.SelectInManager
import com.intellij.ide.SelectInTarget
import com.intellij.ide.StandardTargetWeights
import com.intellij.ide.impl.ProjectViewSelectInTarget
import com.intellij.ide.projectView.ProjectView
import com.intellij.ide.projectView.ProjectViewSettings
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.ProjectAbstractTreeStructureBase
import com.intellij.ide.projectView.impl.ProjectTreeStructure
import com.intellij.ide.projectView.impl.ProjectViewPane
import com.intellij.ide.projectView.impl.nodes.ProjectViewProjectNode
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.ide.util.treeView.NodeDescriptor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.registry.Registry

class ProjectPaneSelectInTarget(project: Project?, protected val id: String) : ProjectViewSelectInTarget(project),
    DumbAware {
    override fun toString(): String {
        return SelectInManager.getProject()
    }

    override fun isSubIdSelectable(subId: String, context: SelectInContext): Boolean {
        return canSelect(context)
    }

    override fun getMinorViewId(): String {
        return id;
    }

    override fun getWeight(): Float {
        return StandardTargetWeights.PROJECT_WEIGHT
    }
}


class ProjectViewPaneTreeStructure(
    private val myProject: Project,
    private val id: String
) : ProjectTreeStructure(myProject, id), ProjectViewSettings {

    override fun createRoot(project: Project, settings: ViewSettings): AbstractTreeNode<*> {
        return ProjectViewProjectNode(project, settings)
    }

    override fun isShowExcludedFiles(): Boolean {
        return ProjectView.getInstance(myProject).isShowExcludedFiles(id)
    }

    override fun isShowLibraryContents(): Boolean {
        return true
    }

    override fun isUseFileNestingRules(): Boolean {
        return ProjectView.getInstance(myProject).isUseFileNestingRules(id)
    }

    override fun isToBuildChildrenInBackground(element: Any): Boolean {
        return Registry.`is`("ide.projectView.ProjectViewPaneTreeStructure.BuildChildrenInBackground")
    }
}


class ReverseOrderAbstractProjectViewPane(project: Project) : ProjectViewPane(project) {
    var ID = "ReverseOrderProject"
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
        return ReverseOrderComparator(myProject, ID)
    }

    override fun createSelectInTarget(): SelectInTarget {
        return ProjectPaneSelectInTarget(myProject, ID)
    }

    override fun createStructure(): ProjectAbstractTreeStructureBase {
        return ProjectViewPaneTreeStructure(this.myProject, ID);
    }
}