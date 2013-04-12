/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.util;

import java.util.ArrayList;
import matrix.AbstractMatrix;
import matrix.dense.DenseMatrix;
import vispipelinebasics.annotations.Param;
import vispipelinebasics.exception.VisPipelineException;
import vispipelinebasics.interfaces.AbstractComponent;
import vispipelinebasics.interfaces.InputInterface;
import vispipelinebasics.interfaces.OutputInterface;

/**
 *
 * @author DaniloEler
 */
public class DifferenceComp implements AbstractComponent<DifferenceComp.Input, DifferenceComp.Output> {

    private static final long serialVersionUID = 1L;

    @Override
    public Output execute(Input in) throws VisPipelineException {
        AbstractMatrix diffMatrix = new DenseMatrix();
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.addAll(in.matrix.getAttributes());
        diffMatrix.setAttributes(attrs);
        ArrayList<Integer> matrixIds = in.matrix.getIds();
        ArrayList<Integer> sampleMatrixIds = in.sampleMatrix.getIds();
        for (int i = 0; i < in.matrix.getRowCount(); i++) {
            Integer id = matrixIds.get(i);
            if (!sampleMatrixIds.contains(id)) {
                diffMatrix.addRow(in.matrix.getRow(i), in.matrix.getLabel(i));
            }
        }
        return new Output(diffMatrix);
    }

    public static class Input implements InputInterface {

        public AbstractMatrix matrix, sampleMatrix;

        public Input(@Param(name = "original matrix") AbstractMatrix matrix,
                @Param(name = "sample matrix") AbstractMatrix sampleMatrix) {
            if (matrix == null || sampleMatrix == null) {
                throw new IllegalArgumentException("A matrix and sample matrix must be provided.");
            }
            this.matrix = matrix;
            this.sampleMatrix = sampleMatrix;
        }
    }

    public static class Output implements OutputInterface {

        public AbstractMatrix diffMatrix;

        public Output(AbstractMatrix diffMatrix) {
            this.diffMatrix = diffMatrix;
        }
    }
}
