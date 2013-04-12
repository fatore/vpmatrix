/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix;

import matrix.dense.DenseMatrix;
import matrix.dense.DenseVector;
import matrix.sparse.SparseMatrix;
import matrix.sparse.SparseVector;

/**
 *
 * @author paulovich
 */
public class VectorFactory {

    public static AbstractVector getInstance(Class matrixtype, float[] vect) {
        return getInstance(matrixtype, vect, 0, 0.0f);
    }

    public static AbstractVector getInstance(Class matrixtype, float[] vect, float klass) {
        return getInstance(matrixtype, vect, 0, klass);

    }

    public static AbstractVector getInstance(Class matrixtype, float[] vect, int id) {
        return getInstance(matrixtype, vect, id, 0.0f);
    }

    public static AbstractVector getInstance(Class matrixtype, float[] vect, int id, float klass) {
        if (matrixtype == DenseMatrix.class) {
            return new DenseVector(vect, id, klass);
        } else if (matrixtype == SparseMatrix.class) {
            return new SparseVector(vect, id, klass);
        }

        return null;
    }

}
