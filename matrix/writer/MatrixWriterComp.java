/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.writer;

import matrix.*;
import java.io.IOException;
import vispipelinebasics.annotations.Param;
import vispipelinebasics.exception.VisPipelineException;
import vispipelinebasics.interfaces.AbstractComponent;
import vispipelinebasics.interfaces.InputInterface;
import vispipelinebasics.interfaces.OutputInterface;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class MatrixWriterComp implements AbstractComponent<MatrixWriterComp.Input, OutputInterface> {

    public static final long serialVersionUID = 1L;
    private String filename;

    @Override
    public OutputInterface execute(Input in) throws VisPipelineException {
        try {
            in.matrix.save(filename);
        } catch (IOException e) {
            throw new VisPipelineException(e);
        }
        return null;
    }

    public static class Input implements InputInterface {

        public AbstractMatrix matrix;

        public Input(@Param(name = "matrix") AbstractMatrix matrix) {
            if (matrix == null) {
                throw new IllegalArgumentException("A matrix must be provided.");
            }
            this.matrix = matrix;
        }
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }
}
