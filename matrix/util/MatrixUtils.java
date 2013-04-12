/* ***** BEGIN LICENSE BLOCK *****
 *
 * Copyright (c) 2005-2007 Universidade de Sao Paulo, Sao Carlos/SP, Brazil.
 * All Rights Reserved.
 *
 * This file is part of Projection Explorer (PEx).
 *
 * How to cite this work:
 *  
 @inproceedings{paulovich2007pex,
 author = {Fernando V. Paulovich and Maria Cristina F. Oliveira and Rosane 
 Minghim},
 title = {The Projection Explorer: A Flexible Tool for Projection-based 
 Multidimensional Visualization},
 booktitle = {SIBGRAPI '07: Proceedings of the XX Brazilian Symposium on 
 Computer Graphics and Image Processing (SIBGRAPI 2007)},
 year = {2007},
 isbn = {0-7695-2996-8},
 pages = {27--34},
 doi = {http://dx.doi.org/10.1109/SIBGRAPI.2007.39},
 publisher = {IEEE Computer Society},
 address = {Washington, DC, USA},
 }
 *  
 * PEx is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * PEx is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 *
 * This code was developed by members of Computer Graphics and Image
 * Processing Group (http://www.lcad.icmc.usp.br) at Instituto de Ciencias
 * Matematicas e de Computacao - ICMC - (http://www.icmc.usp.br) of 
 * Universidade de Sao Paulo, Sao Carlos/SP, Brazil. The initial developer 
 * of the original code is Fernando Vieira Paulovich <fpaulovich@gmail.com>.
 *
 * Contributor(s): Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */
package matrix.util;

import java.util.ArrayList;
import matrix.*;
import matrix.sparse.SparseMatrix;
import matrix.sparse.SparseVector;
import matrix.dense.DenseMatrix;
import matrix.dense.DenseVector;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import visualizationbasics.util.Util;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class MatrixUtils {

    public static AbstractVector mean(AbstractMatrix matrix) {
        assert (matrix.getRowCount() > 0) : "More than zero vectors must be used!";

        if (matrix instanceof SparseMatrix) {
            float[] mean = new float[matrix.getDimensions()];
            Arrays.fill(mean, 0.0f);

            int size = matrix.getRowCount();
            for (int i = 0; i < size; i++) {
                int[] index = ((SparseVector) matrix.getRow(i)).getIndex();
                float[] values = matrix.getRow(i).getValues();

                for (int j = 0; j < index.length; j++) {
                    mean[index[j]] += values[j];
                }
            }

            for (int j = 0; j < mean.length; j++) {
                mean[j] = mean[j] / size;
            }

            return new SparseVector(mean);

        }
        else if (matrix instanceof DenseMatrix) {
            float[] mean = new float[matrix.getDimensions()];
            Arrays.fill(mean, 0.0f);

            int size = matrix.getRowCount();
            for (int i = 0; i < size; i++) {
                float[] values = matrix.getRow(i).getValues();

                for (int j = 0; j < values.length; j++) {
                    mean[j] += values[j];
                }
            }

            for (int j = 0; j < mean.length; j++) {
                mean[j] = mean[j] / size;
            }

            return new DenseVector(mean);
        }

        return null;
    }

    public static SparseMatrix convert(DenseMatrix matrix) {
        SparseMatrix newmatrix = new SparseMatrix();

        newmatrix.setAttributes(matrix.getAttributes());

        for (int i = 0; i < matrix.getRowCount(); i++) {
            AbstractVector dv = matrix.getRow(i);
            newmatrix.addRow(new SparseVector(dv.toArray(), dv.getId(), dv.getKlass()));
        }

        return newmatrix;
    }

    public static DenseMatrix convert(SparseMatrix matrix) {
        DenseMatrix newmatrix = new DenseMatrix();

        newmatrix.setAttributes(matrix.getAttributes());

        for (int i = 0; i < matrix.getRowCount(); i++) {
            AbstractVector dv = matrix.getRow(i);
            newmatrix.addRow(new DenseVector(dv.toArray(), dv.getId(), dv.getKlass()));
        }

        return newmatrix;
    }

    //return two array lists with the minimun and maximum values of each dimension
    //first is the min and the second is the max
    public static ArrayList<ArrayList<Float>> getMinMax(AbstractMatrix matrix) {
        ArrayList<Float> min = new ArrayList<Float>();
        ArrayList<Float> max = new ArrayList<Float>();

        if (matrix.getDimensions() > 0) {
            float[] array = matrix.getRow(0).toArray();
            for (int j = 0; j < array.length; j++) {
                min.add(array[j]);
                max.add(array[j]);
            }

            for (int i = 1; i < matrix.getRowCount(); i++) {
                array = matrix.getRow(i).toArray();

                for (int j = 0; j < array.length; j++) {
                    if (max.get(j) < array[j]) {
                        max.set(j, array[j]);
                    }
                    else if (min.get(j) > array[j]) {
                        min.set(j, array[j]);
                    }
                }
            }
        }

        ArrayList<ArrayList<Float>> maxmin = new ArrayList<ArrayList<Float>>();
        maxmin.add(min);
        maxmin.add(max);

        return maxmin;
    }

    public static ArrayList<MinMax> getMinMax2(AbstractMatrix matrix) {
        ArrayList<MinMax> mm = new ArrayList<MinMax>();

        if (matrix.getDimensions() > 0) {
            for (int i = 0; i < matrix.getDimensions(); i++) {
                mm.add(new MinMax());
            }

            for (int i = 0; i < matrix.getRowCount(); i++) {
                float[] array = matrix.getRow(i).toArray();

                for (int j = 0; j < array.length; j++) {
                    if (mm.get(j).max < array[j]) {
                        mm.get(j).max = array[j];
                    }

                    if (mm.get(j).min > array[j]) {
                        mm.get(j).min = array[j];
                    }
                }
            }
        }

        return mm;
    }

    public static class MinMax {

        public float max = Float.NEGATIVE_INFINITY;
        public float min = Float.POSITIVE_INFINITY;
    }

    /**
     * Returns the transpose of an DenseMatrix, it doesn't preserve the groups
     *
     * @param matrix the DenseMatrix object to be transformed
     * @return a new DenseMatrix
     *
     *
     */
    public static DenseMatrix transpose(AbstractMatrix matrix) {
        DenseMatrix newMatrix = new DenseMatrix();

        for (int i = 0; i < matrix.getDimensions(); i++) {
            float[] newVector = new float[matrix.getRowCount()];
            for (int j = 0; j < matrix.getRowCount(); j++) {
                newVector[j] = matrix.getRow(j).getValue(i);
            }

            String id = (matrix.getAttributes().size() > 0) ? matrix.getAttributes().get(i) : "";

            if (Util.isParsableToInt(id)) {
                newMatrix.addRow(new DenseVector(newVector, Integer.parseInt(id), 0.0f));
            }
            else {
                newMatrix.addRow(new DenseVector(newVector, Util.convertToInt(id), 0.0f), id);
            }


        }
//        System.out.printf("\n");

        // swapping attributes X instance labels 
        ArrayList<String> attributes = new ArrayList<String>();
        for (int j = 0; j < matrix.getRowCount(); j++) {
            attributes.add(matrix.getLabel(j));
        }

        newMatrix.setAttributes(attributes);

        System.out.printf("newMatrix(%dx%d)\n", newMatrix.getRowCount(), newMatrix.getDimensions());

        return newMatrix;
    }

    /**
     * swap the columns(attributes) in a matrix
     *
     * @param matrix
     * @param c1 index of the column1
     * @param c2 index of the column2
     */
    public static void swapColumns(AbstractMatrix matrix, int c1, int c2) {
        try {
            if (c1 != c2) {
                assert (c1 < matrix.getDimensions() && c2 < matrix.getDimensions()) : "wrong index of Columns";

                //swapping attributes
                Collections.swap(matrix.getAttributes(), c1, c2);

                //swapping columns
                Float v1, v2;
                for (int i = 0; i < matrix.getRowCount(); i++) {
                    v1 = matrix.getRow(i).getValue(c1);
                    v2 = matrix.getRow(i).getValue(c2);
                    matrix.getRow(i).setValue(c1, v2);
                    matrix.getRow(i).setValue(c2, v1);
                }

            }
        } catch (Exception e) {
            System.out.printf("Exception: %s\n", e.toString());
        }
    }

    /**
     * swap the rows(instances) in a matrix
     *
     * @param matrix
     * @param row1 index of the row1
     * @param row2 index of the row2
     */
    public static void swapRows(AbstractMatrix matrix, int row1, int row2) {
        AbstractVector vector1 = matrix.getRow(row1);
        String label1= matrix.getLabel(row1);
        AbstractVector vector2 = matrix.getRow(row2);
        String label2= matrix.getLabel(row2);
        //swapping
        matrix.getLabels().set(row2, label1);
        matrix.getLabels().set(row1, label2);
        matrix.setRow(row1, vector2);
        matrix.setRow(row2, vector1);

    }

    private static int lookForIndexInArray(int value, int[] anArray) {
        for (int i = 0; i < anArray.length; i++) {
            if (anArray[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static void sortByDimension(AbstractMatrix matrix, int dimension) {
        Float[][] valueAndIndex = new Float[matrix.getRowCount()][2];
        int[] oldOrder = new int[matrix.getRowCount()];
        for (int i = 0; i < valueAndIndex.length; i++) {
            valueAndIndex[i][0] = matrix.getRow(i).getValue(dimension);
            valueAndIndex[i][1] = new Float(i);
            oldOrder[i] = i;
        }
        Arrays.sort(valueAndIndex, new Comparator<Float[]>() {

            @Override
            public int compare(final Float[] first, final Float[] second) {
                return first[0].compareTo(second[0]);
            }

        });


        for (int i = 0; i < valueAndIndex.length; i++) {
            int r1 = lookForIndexInArray(valueAndIndex[i][1].intValue(), oldOrder);
            int r2 = i;
            MatrixUtils.swapRows(matrix, r1, r2);
            int tempValue = oldOrder[r1];
            oldOrder[r1] = oldOrder[r2];
            oldOrder[r2] = tempValue;
        }


    }

    public static void sortByInstance(AbstractMatrix matrix, int instance) {
        Float[][] valueAndIndex = new Float[matrix.getDimensions()][2];
        int[] oldOrder = new int[matrix.getDimensions()];
        for (int i = 0; i < valueAndIndex.length; i++) {
            valueAndIndex[i][0] = matrix.getRow(instance).getValue(i);
            valueAndIndex[i][1] = new Float(i);
            oldOrder[i] = i;
        }

        Arrays.sort(valueAndIndex, new Comparator<Float[]>() {

            @Override
            public int compare(final Float[] first, final Float[] second) {
                return first[0].compareTo(second[0]);
            }

        });



        for (int i = 0; i < valueAndIndex.length; i++) {
            int c1 = lookForIndexInArray(valueAndIndex[i][1].intValue(), oldOrder);
            int c2 = i;
            MatrixUtils.swapColumns(matrix, c1, c2);
            int tempValue = oldOrder[c1];
            oldOrder[c1] = oldOrder[c2];
            oldOrder[c2] = tempValue;
        }

    }

    public static double[] getColumn(AbstractMatrix matrix, int column) {
        int numInstances = matrix.getRowCount();
        double[] aColumn = new double[numInstances];
        for (int i = 0; i < numInstances; i++) {
            aColumn[i] = matrix.getRow(i).getValue(column);
        }
        return aColumn;
    }

    public static double[] getColumn(float[][] matrix, int column) {
        int numInstances = matrix.length;
        double[] aColumn = new double[numInstances];
        for (int i = 0; i < numInstances; i++) {
            aColumn[i] = matrix[i][column];
        }
        return aColumn;
    }

    public static void reorder(AbstractMatrix matrix, int[] newOrderPositions) {
        assert (matrix.getRowCount() != newOrderPositions.length) : "Matrix and newOrderPositions must have the same size!";
        for (int i = 0; i < newOrderPositions.length - 1; i++) {
            MatrixUtils.swapRows(matrix, i, newOrderPositions[i]);
        }

    }

}
