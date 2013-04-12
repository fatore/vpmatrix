/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.reader;

import java.io.IOException;
import matrix.*;
import vispipelinebasics.annotations.VisComponent;
import vispipelinebasics.exception.VisPipelineException;
import vispipelinebasics.interfaces.AbstractComponent;
import vispipelinebasics.interfaces.InputInterface;
import vispipelinebasics.interfaces.OutputInterface;

/**
 *
 * @author Fernando Vieira Paulovich
 */
@VisComponent(hierarchy = "Points.Input",
name = "Points matrix reader",
description = "Read a Points Matrix from a file.")
public class MatrixReaderComp implements AbstractComponent<MatrixReaderComp.Input, MatrixReaderComp.Output> {

    private static final long serialVersionUID = 1L;

    public Output execute(Input in) throws VisPipelineException {
        try {
            return new Output(MatrixFactory.getInstance(in.filename));
        } catch (IOException e) {
            throw new VisPipelineException(e);
        }
    }

    public static class Input implements InputInterface {

        public String filename;

        public Input(String filename) {
            this.filename = filename;
        }
    }

    public static class Output implements OutputInterface {

        public AbstractMatrix matrix;

        public Output(AbstractMatrix matrix) {
            this.matrix = matrix;
        }
    }
}
