package com.example;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.*;

import java.io.*;

public class Example {

    public static void main(String[] args)

    {
        try {
            // 1) Load Docx file by filling Velocity template engine and cache it to the registry

            /*
             * Important limiatation
             * https://github.com/opensagres/xdocreport/wiki/DocxDesignReport#create-mergefield-with-ms-word
             */

            InputStream in = Example.class.getResourceAsStream("/poc.docx");
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);

            // 2) Create context Java model
            IContext context = report.createContext();
            context.put("who", "World");
            context.put("other", "Another");

            // 3) Generate report by merging Java model with the Docx
            OutputStream out = new FileOutputStream(new File("poc_out.docx"));
            report.process(context, out);

            FieldsExtractor<FieldExtractor> extractor = FieldsExtractor.create();
            report.extractFields(extractor);

            // 3) Generate report by merging Java model with the Docx
            for (FieldExtractor fieldExtractor : extractor.getFields()) {
                System.out.println(fieldExtractor.getName());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
    }

}
