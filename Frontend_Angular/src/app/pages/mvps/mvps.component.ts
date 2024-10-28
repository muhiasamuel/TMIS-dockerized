import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpServiceService } from '../../services/http-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { forkJoin } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MvpsAssessmentComponent } from './mvps-assessment/mvps-assessment.component';
import { jsPDF } from 'jspdf';
import * as XLSX from 'xlsx';

import html2canvas from 'html2canvas';
import moment from 'moment';

@Component({
  selector: 'app-mvps',
  templateUrl: './mvps.component.html',
  styleUrls: ['./mvps.component.scss']
})
export class MvpsComponent implements OnInit {
  isDisabled = true;
  authUser: any;
  performance: any[] = [];
  talentData: any[] = [];
  MVPs: any[] = [];
  //data
  attrition: FormGroup;
  market_Exposure: FormGroup;
  careerPriority: FormGroup;
  retentionState: FormGroup;
  assessment: any[] = [];
  fullData: any[] = [];
  filteredData:any[] = [];
  allData:any[]=[];
  filteredMVPs:any[] = [];
  paginatedMVPs: any[] = [];
  filterText: string = '';
  currentPage: number = 1;
  itemsPerPage: number = 5;
  totalPages: number = 1;
  @ViewChild('pdf', {static:false}) el!:ElementRef


  constructor(
    private http: HttpServiceService,
    public dialog: MatDialog,
    private snackBar: MatSnackBar,
    public fb: FormBuilder
  ) {
    this.attrition = this.fb.group({
      impactOfAttrition: ['', Validators.required]
    });

    this.market_Exposure = this.fb.group({
      marketExposure: ['', [Validators.required]]
    });

    this.careerPriority = this.fb.group({
      careerPriorities: ['', [Validators.required]]
    });

    this.retentionState = this.fb.group({
      retentionAssessmentState: ['', [Validators.required]]
    });

  }

  ngOnInit(): void {
    const user = localStorage.getItem("user");
    if (user) {
      this.authUser = JSON.parse(user);
    }
    this.processMvps();
  }

  ngAfterViewInit(): void {
    //Called after ngAfterContentInit when the component's view has been initialized. Applies to components only.
    //Add 'implements AfterViewInit' to the class.

  }

  processMvps(): void {
    this.getMVPsAssessment()
    const url = `${this.http.serverUrl}MVPs/employees/${this.authUser.user.userId}`;
    forkJoin({
      talentData: this.http.getTalent(this.authUser.user.userId),
      performance: this.http.getThreeYearsEmpPerformances(this.authUser.user.userId)
    }).pipe(
      catchError(error => {
        this.snackBar.open(error.error.message, "Close", { duration: 3600 });
        throw error;
      })
    ).subscribe(({ talentData, performance }) => {
      this.talentData = talentData.items;
      this.performance = performance.item;
      console.log("qwertic", performance.item);
      this.combineData();
      this.fullcombineData()
      this.filterData()
      this.filterMVPs()
      this.updatePagination();

    });
  }

  combineData(): void {
    
    const combinedData = this.talentData.map(user => {
      const performance = this.performance.find(p => p.userId == user.userId);
      return {
        userId: user.userId,
        name: user.userFullName,
        talentRating: user.talentRating,
        potentialRating: user.potentialRating,
        Year: user.performanceYear,
        threeYearsPerformanceRating: performance.totalPerformanceRating,
        yearsRatedCount: performance.yearsCount,
        performances: performance.performances
        // Add other performance properties here
      };

    });

    this.MVPs = combinedData;
    console.log("1233444",performance);
    

  }

  processValues(id: number) {
    const data = {
      "impactOfAttrition": this.attrition?.get('impactOfAttrition')?.value,
      "marketExposure": this.market_Exposure?.get('marketExposure')?.value,
      "careerPriorities": this.careerPriority?.get('careerPriorities')?.value,
      "retentionAssessmentState": this.retentionState?.get('retentionAssessmentState')?.value,
    }
    if (data.impactOfAttrition == null || data.impactOfAttrition == "") {
      this.snackBar.open("Impact of attrition on business Appears to be empty. Kindly Fill the field", "Close", { duration: 3600, verticalPosition: 'top' })
    } else if (data?.marketExposure == null || data?.marketExposure == "") {
      this.snackBar.open("Employee Talent Market Exposure Appears to be empty. Kindly Fill the field", "Close", { duration: 3600, verticalPosition: 'top' })
    } else if (data.careerPriorities == null || data?.careerPriorities == "") {
      this.snackBar.open("Employee Career Priorities Appears to be empty. Kindly Fill the field", "Close", { duration: 3600, verticalPosition: 'top' })
    } else if (data?.retentionAssessmentState == null || data?.retentionAssessmentState == "") {
      this.snackBar.open("Employee Current Retention Assessment Appears to be empty. Kindly Fill the field", "Close", { duration: 3600, verticalPosition: 'top' })
    } else {
      const dialogRef: MatDialogRef<MvpsAssessmentComponent> = this.dialog.open(MvpsAssessmentComponent, {
        width: "60%",
        data: {
          employeeId: id,
          assessment: data
        }
      })

      // Handle the dialog result (if needed)
      dialogRef.afterClosed().subscribe((result) => {
        console.log(`Dialog result: ${result}`);
        this.ngOnInit();
      });
    }

  }

  addStrategies(id: number) {
    this.processValues(id)

  }
  //http://localhost:8080/v1/MVPs/employees/1
  getMVPsAssessment() {
    const url = `${this.http.serverUrl}MVPs/employees/${this.authUser.user.userId}`;
    this.http.getData(url).subscribe(
      ((res) => {
        this.assessment = res.item
        console.log("assesssment", res);

      }),
      ((error) => {
        this.snackBar.open(error.error.message, "Close", { duration: 3600, verticalPosition: 'top', direction: 'rtl' })
      }),
      () => {

      }
    )
  }

  filterData() {
    this.filteredData = this.fullData.filter(item => item.retentionState !== undefined && item.retentionState !== null && item.retentionState !== '');
  }

  filterMVPs() {
    console.log('Full Data:', this.fullData);
    this.filteredMVPs = this.fullData
      .filter(item => {
        console.log('Filtering item:', item);
        return (item.yearsRatedCount >= 1 &&
                item.threeYearsPerformanceRating >= 1 &&
                (item.talentRating == 'A1' ||
                 item.talentRating == 'A2' ||
                 item.talentRating == 'B1' ||
                 item.talentRating == 'B2'));
      });
      this.allData = this.filteredMVPs
    console.log('Filtered MVPs:', this.filteredMVPs);
  }
  
  
  fullcombineData(): void {

    const combinedData = this.talentData.map(user => {
      const performance = this.performance.find(p => p.userId == user.userId);
      const assesment = this.assessment.find(a => a.employeeId == user.userId);
      return {
        userId: user?.userId,
        name: user?.userFullName,
        department: user?.departmentName,
        position: user?.positionName,
        pf:user?.pf_No,
        talentRating: user?.talentRating,
        potentialRating: user?.potentialRating,
        Year: user?.performanceYear,
        impactOfAttrition: assesment?.impactOfAttrition,
        marketExposure: assesment?.marketExposure,
        retentionState: assesment?.retentionState,
        strategies: assesment?.strategies,
        careerPriority: assesment?.careerPriority,
        threeYearsPerformanceRating: performance?.totalPerformanceRating,
        yearsRatedCount: performance?.yearsCount,
        performances: performance?.performances
        // Add other performance properties here
      };
    })

    this.fullData = combinedData
    console.log('Full data', combinedData);
    

  }

    // Download PDF using jsPDF
    downloadPdf() { 
      const pdf = new jsPDF('p', 'pt', 'a4');
      const content = this.el.nativeElement;
      const padding = 20; // Padding in pixels
      const pageHeight = pdf.internal.pageSize.height;
      let position = 0;
      let totalPages = 1;
    
      html2canvas(content).then(canvas => {
        const imgData = canvas.toDataURL('image/png');
        const imgWidth = pdf.internal.pageSize.width - (2 * padding); // Subtract padding from the width
        const titleHeight = 40; // Title height in pixels
    
        const imgHeight = (canvas.height * imgWidth) / canvas.width;
        let heightLeft = imgHeight + titleHeight;
    
        // Calculate the scale factor to fit the width of the page
        const scaleFactor = imgWidth / canvas.width;
    
        while (heightLeft >= 0) {
          position = heightLeft - imgHeight;
    
          // Add a new page if content exceeds the page height
          if (position < -pageHeight) {
            pdf.addPage();
            totalPages++;
            position = 0;
          }
    
          pdf.addImage(imgData, 'PNG', padding, padding + titleHeight + position, imgWidth, imgHeight, undefined, 'FAST');
          heightLeft -= pageHeight - (2 * padding) - titleHeight;
    
          if (heightLeft > 0) {
            pdf.addPage();
            totalPages++;
          }
        }
    
              // Get the current date
    const now = moment();

    // Format the date as "25th October 2024"
    const formattedDate = now.format("Do MMMM YYYY");
        // Add title to first page
        pdf.setFillColor(163, 43, 41);
        pdf.rect(padding, padding, pdf.internal.pageSize.width - (2 * padding), titleHeight, 'F');
        pdf.setTextColor(255, 255, 255);
        pdf.setFontSize(16);
        pdf.text('Most Valuable Players Assessments Report |' +  'Date:  '  + `${formattedDate}`, padding + 10, padding + 25);
    
        // Add pagination
        for (let i = 1; i <= totalPages; i++) {
          pdf.setPage(i);
          pdf.setTextColor(0, 0, 0);
          pdf.setFontSize(12);
          pdf.text(`Page ${i} of ${totalPages}`, pdf.internal.pageSize.width - padding - 50, pdf.internal.pageSize.height - padding);
        }
    
        pdf.save('MVPsAssessment.pdf');
      });
    }

    //download excel
    downloadExcel(): void {
      // Create a new workbook
      const workbook: XLSX.WorkBook = XLSX.utils.book_new();
      
      // Prepare data for the Excel file
      const excelData = this.filteredData.map(mvp => ({
        'Name': mvp.name,
        'PF Number': mvp.pf,
        'Department': mvp.department,
        'position': mvp.position,
        'Talent Rating': mvp.talentRating,
        'Potential Rating': mvp.potentialRating,
        'Year': mvp.Year,
        'Impact of Attrition': mvp.impactOfAttrition,
        'Market Exposure': mvp.marketExposure,
        'Retention State': mvp.retentionState,
        'Strategies': Array.isArray(mvp.strategies) 
        ? mvp.strategies.map(strategy => strategy.retentionStrategies).join(', ') 
        : '', // Properly joining strategies        'Career Priority': mvp.careerPriority,
        '3 Years Performance Rating': mvp.threeYearsPerformanceRating,
        'Years Rated Count': mvp.yearsRatedCount,
      }));
      
      // Convert data to a worksheet
      const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(excelData);
      
      // Add the worksheet to the workbook
      XLSX.utils.book_append_sheet(workbook, worksheet, 'MVPs');
    
      // Export the Excel file
      XLSX.writeFile(workbook, 'MVPsAssessment.xlsx');
    }
    

    filterSearchData(): void {
      this.filteredMVPs = this.allData.filter(item => 
        item.name.toLowerCase().includes(this.filterText.toLowerCase())
      );
      this.updatePagination();
      console.log("123", this.fullData);
      
    }
  
    updatePagination(): void {
      this.totalPages = Math.ceil(this.filteredMVPs.length / this.itemsPerPage);
      this.paginate();
    }
  
    paginate(): void {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      this.paginatedMVPs = this.filteredMVPs.slice(start, end);
    }
  
    prevPage(): void {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.paginate();
      }
    }
  
    nextPage(): void {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.paginate();
      }
    }

    //d
}
