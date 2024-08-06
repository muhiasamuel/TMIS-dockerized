package com.example.talent_man.controllers;

import com.example.talent_man.models.RolesAssessment;
import com.example.talent_man.repos.CriticalRolesAssessmentRepo;
import com.example.talent_man.services.CriticalRolesAssessmentService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api")
public class ReportsController {



    @Autowired
    CriticalRolesAssessmentService rolesAssessmentService;

    @Autowired
    CriticalRolesAssessmentRepo criticalRolesAssessmentrepo;
    @GetMapping("/generate/criticalRolesReport/{fileType}")
    private byte[] generateReport(String fileType) {
        try {
            // Load and compile the JasperReport template
            File file = ResourceUtils.getFile("classpath:reports/critical_Roles.jrxml");

            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

            // Set up parameters for the report (if any)
            Map<String, Object> parameters = new HashMap<>();

            // Set up data source (dummy data for demonstration)
            List<RolesAssessment> criticalRoles = criticalRolesAssessmentrepo.findAll(); // Your method to fetch critical roles

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(criticalRoles);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);
           // return exportExcelReport(jasperPrint);
//
          //  return JasperExportManager.exportReportToPdf(jasperPrint);
            // Export the report based on the specified file type
//            switch (fileType) {
//                case "pdf":
//                    return JasperExportManager.exportReportToPdf(jasperPrint);
//                case "xml":
//                    return JasperExportManager.exportReportToXml(jasperPrint).getBytes();
//                case "csv":
//                    return generateCsvReport().getBytes();
//                case "excel":
//                    return exportExcelReport(jasperPrint);
//                default:
//                    return "Unsupported file type".getBytes();
//            }
        } catch (JRException | FileNotFoundException e) {
            e.printStackTrace();
            return "Failed to generate report".getBytes();
        }


    }
    private String generateXmlReport() {
        // Logic to generate XML report
        return "XML report generated";
    }

    private String generateCsvReport() {
        // Logic to generate CSV report
        return "CSV report generated";
    }
    private byte[] exportExcelReport(JasperPrint jasperPrint) throws JRException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JRXlsxExporter exporter = new JRXlsxExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

        exporter.exportReport();


        return outputStream.toByteArray();
    }

//    @GetMapping("/generate/criticalRolesReport/{fileType}")
//    public byte[] generateReport(@PathVariable String fileType) {
//        // Generate report based on fileType
//        switch (fileType) {
//            case "pdf":
//                // Generate PDF report
//                return generatePdfReport();
//            case "xml":
//                // Generate XML report
//                return generateXmlReport().getBytes();
//            case "csv":
//                // Generate CSV report
//                return generateCsvReport().getBytes();
//            case "excel":
//                // Generate Excel report
//                return generateExcelReport().getBytes();
//            default:
//                return "Unsupported file type".getBytes();
//        }
//    }
//
//    // Implement methods to generate different types of reports
//    private byte[] generatePdfReport() {
//        try {
//            // Load and compile the JasperReport template
//            File file = ResourceUtils.getFile("classpath:reports/critical_Roles.jrxml");
//
//            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
//
//            // Set up parameters for the report (if any)
//            Map<String, Object> parameters = new HashMap<>();//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
//
//            // Set up data source (dummy data for demonstration)
//            List<CriticalRolesAssessmentRepo.RolesAssessmentInterface> criticalRoles = criticalRolesAssessmentrepo.findAllDetails(); // Your method to fetch critical roles
//
//            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(criticalRoles);
//
//            // Fill the report with data
//
//            // Export the report to PDF
//             JasperExportManager.exportReportToPdf(jasperPrint);
//
//            return JasperExportManager.exportReportToPdf(jasperPrint);
//        } catch (JRException | FileNotFoundException e) {
//            e.printStackTrace();
//            return "Failed to generate PDF report".getBytes();
//        }
//
//    }
//
//    private String generateXmlReport() {
//        // Logic to generate XML report
//        return "XML report generated";
//    }
//
//    private String generateCsvReport() {
//        // Logic to generate CSV report
//        return "CSV report generated";
//    }
//
//    private String generateExcelReport() {
//        // Logic to generate Excel report
//        return "Excel report generated";
//    }
}
