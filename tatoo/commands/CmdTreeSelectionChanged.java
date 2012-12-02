package tatoo.commands;

import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import tatoo.Command;

public class CmdTreeSelectionChanged implements Command {

    TreeSelectionModel treeModel;
    TreePath           oldPath;
    TreePath           newPath;

    public CmdTreeSelectionChanged (TreeSelectionModel selectionModel, TreePath oldPath, TreePath newPath) {
        this.treeModel = selectionModel;
        this.oldPath = oldPath;
        this.newPath = newPath;
    }

    @Override
    public void execute () {
        treeModel.setSelectionPath (newPath);
    }

    @Override
    public void undo () {
        treeModel.setSelectionPath (oldPath);
    }

    @Override
    public void redo () {
        treeModel.setSelectionPath (newPath);
    }

    @Override
    public void writeObject () {/* Nothing to do */}

    @Override
    public void deleteObject () {/* nothing to do */}

}
