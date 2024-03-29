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
package matrix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public abstract class AbstractMatrix implements Cloneable {
        
    protected int dimensions;
    protected ArrayList<String> attributes = new ArrayList<String>();
    protected ArrayList<String> labels = new ArrayList<String>();
    protected ArrayList<AbstractVector> rows = new ArrayList<AbstractVector>();

    public abstract void load(String filename) throws IOException;

    public abstract void save(String filename) throws IOException;

    @Override
    public abstract Object clone() throws CloneNotSupportedException;

    public boolean contains(AbstractVector vector) {
        return this.rows.contains(vector);
    }

    public void addRow(AbstractVector vector) {
        assert (rows.isEmpty() || this.dimensions == vector.size()) :
                "ERROR: vector of wrong size!";

//        this.labels.add(""); //this can cause a bug when reading/writting the matrix
        this.rows.add(vector);
        this.dimensions = vector.size();
    }

    public void addRow(AbstractVector vector, String label) {
        assert (rows.isEmpty() || this.dimensions == vector.size()) :
                "ERROR: vector of wrong size!";

        this.labels.add(label);
        this.rows.add(vector);
        this.dimensions = vector.size();
    }

    public void setRow(int index, AbstractVector vector) {
        assert (rows.size() > index && this.dimensions == vector.size()) :
                "ERROR: wrong index or vector of wrong size!";

        this.rows.set(index, vector);
    }

    public void setRow(int index, AbstractVector vector, String label) {
        assert ((rows.size() > index && this.dimensions == vector.size())
                || labels.size() > index && this.dimensions == vector.size()) :
                "ERROR: wrong index or vector of wrong size!";

        this.labels.set(index, label);
        this.rows.set(index, vector);
    }
    
    public void setKlass(float[] klass) throws IllegalArgumentException {
        
        if (klass.length != rows.size()) {
            
            throw new IllegalArgumentException("Arguments must have the same "
                    + "number of rows of the matrix.");
        }
        
        for (int i = 0; i < rows.size(); i++) {
            
            rows.get(i).setKlass(klass[i]);
        }
    }

    public AbstractVector removeRow(int index) {
        assert (rows.size() > index) : "ERROR: wrong index!";

        if (this.labels.size() > 0) {
            this.labels.remove(index); //remove the label
        }

        return this.rows.remove(index);
    }

    public int getRowCount() {
        return rows.size();
    }

    public int getDimensions() {
        return dimensions;
    }

    public ArrayList<AbstractVector> getRows() {
        return rows;
    }

    public AbstractVector getRow(int row) {
        assert (rows.size() > row) :
                "ERROR: this row does not exists in the matrix!";

        return this.rows.get(row);
    }

    public String getLabel(int row) {
        assert ((labels.isEmpty() || labels.size() > row) && rows.size() > row) :
                "ERROR: this row does not exists in the labels!";

        if (labels.size() > 0) {
            return labels.get(row);
        } else {
            return Integer.toString(rows.get(row).getId());
        }
    }

    public void setLabels(ArrayList<String> labels) {
        assert (this.labels.isEmpty() || rows.size() == labels.size()) :
                "ERROR: labels and matrix of different sizes!";

        this.labels = labels;
    }

    public void normalize() {
        int size = this.rows.size();

        for (int i = 0; i < size; i++) {
            if (!this.rows.get(i).isNull()) {
                this.rows.get(i).normalize();
            } else {
                Logger.getLogger(AbstractMatrix.class.getName()).log(Level.INFO,
                        "Ignoring null vector on the normalization.");
            }
        }
    }

    public float[][] toMatrix() {
        float[][] matrix = new float[this.rows.size()][];

        int size = this.rows.size();
        for (int i = 0; i < size; i++) {
            matrix[i] = this.rows.get(i).toArray();
        }

        return matrix;
    }

    public ArrayList<String> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(ArrayList<String> attributes) {
        assert (rows.isEmpty() || this.dimensions == attributes.size()) :
                "ERROR: attributes and vectors of different sizes!";

        this.attributes = attributes;
    }

    public ArrayList<Integer> getIds() {
        ArrayList<Integer> ids = new ArrayList<Integer>();

        int size = this.rows.size();
        for (int i = 0; i < size; i++) {
            ids.add(this.rows.get(i).getId());
        }

        return ids;
    }

    public float[] getClassData() {
        float[] cdata = new float[this.rows.size()];

        for (int i = 0; i < cdata.length; i++) {
            cdata[i] = this.rows.get(i).getKlass();
        }

        return cdata;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }
}
