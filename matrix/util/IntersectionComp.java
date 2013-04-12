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
public class IntersectionComp implements AbstractComponent<IntersectionComp.Input, IntersectionComp.Output> {

    private static final long serialVersionUID = 1L;

    @Override
    public Output execute(Input in) throws VisPipelineException {
        AbstractMatrix intersectionMatrix = new DenseMatrix();
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.addAll(in.matrixA.getAttributes());
        intersectionMatrix.setAttributes(attrs);
        ArrayList<Integer> matrixAIds = in.matrixA.getIds();
        ArrayList<Integer> matrixBIds = in.matrixB.getIds();
        for (int i = 0; i < in.matrixA.getRowCount(); i++) {
            Integer id = matrixAIds.get(i);
            if (matrixBIds.contains(id)) {
                intersectionMatrix.addRow(in.matrixA.getRow(i), in.matrixA.getLabel(i));
            }
        }
        return new Output(intersectionMatrix);
    }

    public static class Input implements InputInterface {

        public AbstractMatrix matrixA, matrixB;

        public Input(@Param(name = "matrix A") AbstractMatrix matrixA, @Param(name = "matrix B") AbstractMatrix matrixB) {
            if (matrixA == null || matrixB == null) {
                throw new IllegalArgumentException("Matrix A and matrix B must be provided.");
            }
            this.matrixA = matrixA;
            this.matrixB = matrixB;
        }
    }

    public static class Output implements OutputInterface {

        public AbstractMatrix intersectionMatrix;

        public Output(AbstractMatrix intersectionMatrix) {
            this.intersectionMatrix = intersectionMatrix;
        }
    }
}
