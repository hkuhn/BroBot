package learning.util;

import april.jmat.Matrix;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Looks to read in data for a multi-input mapping. Data is written one entry per line like:
 *
 * (a,b,c,d) = f
 *
 * User: slessans
 * Date: 4/17/13
 * Time: 3:08 AM
 */
public class DataReader {

    protected File inputFile;
    protected Matrix parsedInput;
    protected Matrix parsedOutput;

    public DataReader(File inputFile) {
        this.inputFile = inputFile;
    }

    public File getInputFile() {
        return inputFile;
    }

    public Matrix getParsedInput() {
        return parsedInput;
    }

    public Matrix getParsedOutput() {
        return parsedOutput;
    }

    public void parse() throws IOException, DataReaderParseException {

        this.parsedInput = null;
        this.parsedOutput = null;

        List<List<Double>> inputs = new LinkedList<List<Double>>();
        List<Double> outputs = new LinkedList<Double>();

        BufferedReader reader = new BufferedReader(new FileReader(this.getInputFile()));

        Pattern inputStructure = Pattern.compile("^\\s*\\(([\\-0-9., \t]+)\\)\\s*=\\s*([\\-0-9.]+)\\s*$");

        String line = null;
        int lineNumber = 0;
        Integer inputTupleLength = null;
        while ( (line = reader.readLine()) != null ) {
            lineNumber++;

            // trim it
            line = line.trim();

            // ignore whitespace
            if ( line.length() == 0 ) continue;

            // parse line
            Matcher matcher = inputStructure.matcher(line);
            if ( matcher.matches() ) {
                final String inputTupleString = matcher.group(1);
                final String outputNumberString = matcher.group(2);

                Double output = null;
                try {
                    output = Double.parseDouble(outputNumberString);
                } catch (NumberFormatException e) {
                    throw new DataReaderParseException(
                            "Could not parse output number on line " + lineNumber + ": " +
                            "(input string: '" + outputNumberString + "') : " +
                            e.getLocalizedMessage()
                    );
                }

                // input should be comma separated numbers
                String [] inputNumberStrings = inputTupleString.split(",");
                if ( inputTupleLength == null ) {
                    inputTupleLength = inputNumberStrings.length;
                } else {
                    if ( inputNumberStrings.length != inputTupleLength ) {
                        throw new DataReaderParseException("Could not parse input tuple on line " + lineNumber +
                                ": number of elements (" + inputNumberStrings.length + ") does not equal previously " +
                                "set number (" + inputTupleLength + ") [string to be parsed: '" + inputTupleString + "']");
                    }
                }

                ArrayList<Double> inputNumbers = new ArrayList<Double>(inputTupleLength);
                for (String inputNumberString : inputNumberStrings) {
                    try {
                        inputNumbers.add(Double.parseDouble(inputNumberString));
                    } catch (NumberFormatException e) {
                        throw new DataReaderParseException(
                                "Could not parse input number on line " + lineNumber + ": " +
                                        "(input string: '" + inputNumberString + "') : " +
                                        e.getLocalizedMessage()
                        );
                    }
                }

                inputs.add(inputNumbers);
                outputs.add(output);

            } else {
                throw new DataReaderParseException("Does not match input structure on line " + lineNumber);
            }

            this.parsedOutput = new Matrix(outputs.size(), 1);
            this.parsedInput = new Matrix(inputs.size(), inputTupleLength);

            int row = 0;
            for (Double output : outputs) {
                this.parsedOutput.set(row, 0, output);
                row++;
            }

            row = 0;
            for (List<Double> tuple : inputs) {
                int col = 0;
                for (Double value : tuple) {
                    this.parsedInput.set(row, col, value);
                    col++;
                }
                row++;
            }
        }


    }

    public class DataReaderParseException extends Exception {
        public DataReaderParseException(String message) {
            super(message);
        }
    }

}
