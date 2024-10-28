import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpServiceService } from '../../services/http-service.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { UserData } from '../skills-view/skills-view.component';
import * as XLSX from 'xlsx';

import { jsPDF } from 'jspdf';
import html2canvas from 'html2canvas';

import { callback } from 'chart.js/dist/helpers/helpers.core';
import { HiposInterventionsComponent } from './hipos-interventions/hipos-interventions.component';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, forkJoin } from 'rxjs';
import moment from 'moment';


interface Employee {
  department: any;
  position: any;
  pfNo: any;
  empId: string;
  empName: string;
  attScores: AttScore[];
  averageScore: string;
  potentialRating: string;
  performanceRatings: string;
  talentRating: string;
  proposedDevelopmentInterventions:string;
  potentialNextRoles:string;

}

interface AttScore {
  id: string;
  score: string;
  pattName: string;
}

interface ApiResponse {
  item: Employee[];
  status: number;
  message: string;
}

@Component({
  selector: 'app-hipos',
  templateUrl: './hipos.component.html',
  styleUrls: ['./hipos.component.scss']
})
export class HIPOsComponent implements OnInit {
  managerId: any;
  mappedSuccession:FormGroup;
  hiposdata: any[] = [];
  performance: any[] = [];
  interventions: any[] = [];
  allHIPOsData: any[] = [];
  filteredHIPOsData:any [] = [];
  data: any[] = [];
  paginatedHIPOS: any[] = [];
  filterText: string = '';
  currentPage: number = 1;
  itemsPerPage: number = 5;
  totalPages: number = 1;
  displayedColumns: string[] = [
    'userId',
     'username',
      'aspiration', 
      'judgement',
       'drive', 
       'changeAgility',
        'averageScore', 
        'potentialRating',
         'performanceRatings', 
         'talentRating',
         'potentialNextRoles',
         'proposedDevelopmentInterventions',
         ];
  dataSource: MatTableDataSource<Employee>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild('pdfarea', {static:false}) el!:ElementRef
  constructor(private http: HttpClient, 
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    public dialog: MatDialog,
    private https: HttpServiceService) { 
      this.mappedSuccession = fb.group({
        potentialNextRole: ['', Validators.required]
      })
    }

  ngOnInit(): void {
    const user = localStorage.getItem("user");
    this.managerId = JSON.parse(user);
    console.log('managerid', this.managerId);

    this.getHipoData();
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }


  getHipoData() {
    //http://192.168.88.6:8080/v1/performances/employees/1
    //http://192.168.88.6:8080/v1/hipos/interventions/1
    const performaceUrl = `${this.https.serverUrl}performances/employees/${this.managerId.user.userId}`;
    const interventionsUrl = `${this.https.serverUrl}hipos/interventions/${this.managerId.user.userId}`
    const url = `${this.https.serverUrl}performances/HIPOs/all/employees/${this.managerId.user.userId}`;
    forkJoin({
      hipos: this.https.getHipos(url),
      performance: this.https.getData(performaceUrl),
      interventions: this.https.getData(interventionsUrl) 
    }).pipe(
      catchError(error => {
        this.snackBar.open(error.error.message, "Close", {duration:3600, verticalPosition:"top"});
        throw error;
      })
    ).subscribe(
      ({hipos, performance, interventions}) => {

        this.hiposdata = hipos.items;
        this.performance = performance.item;
        this.interventions = interventions.item;
        this.combineData()
        this.filteredData()
        this.updatePagination();
      }
    )
  }
    filteredData(){
      if (this.allHIPOsData) {
        this.filteredHIPOsData = this.allHIPOsData.filter(h => h.nextPotentialRole != '' && h.nextPotentialRole != undefined && h.nextPotentialRole != null)
      }
    }

  combineData(){
    const allData = this.hiposdata.map(user => {
      const performance = this.performance.find(p => p.userId == user.userId);
      const interventions = this.interventions.find(i => i.userId == user.userId)
      return {
        userId:user?.userId,
        pfNo: user?.pf_No,
        department: user?.departmentName,
        position: user?.positionName,
        name: user?.userFullName,
        aspiration: user?.aspirationScore,
        judgement: user?.judgmentScore,
        drive: user?.driveScore,
        changeAgility: user?.changeAgilityScore,
        potentialAverage: user?.averagePotential,
        potentialRating: user?.potentialRating,
        performanceRating: performance?.performances,
        talentRating: user?.talentRating,
        nextPotentialRole: interventions?.nextPotentialRole,
        interventions: interventions?.interventions
      }
    })
    console.log("12345678", allData);
    this.data = allData;
    this.allHIPOsData = allData
    this.filterSearchData();
    
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
      pdf.text('HIPOs Assessment Report |' +  'Date:  '  + `${formattedDate}`, padding + 10, padding + 25);
  
     
  
      pdf.save('HIPOs Assessment.pdf');
    });
  }

  // downloading excel 
  downloadExcel() {
    if (!this.allHIPOsData || this.allHIPOsData.length === 0) {
        console.error('No data available to export.');
        return; // Exit the function if no data is available
    }

    const dataToExport = this.filteredHIPOsData.map((hipo: Employee) => ({
        Name: hipo.name,
        pfNo:hipo.pfNo,
        Department:hipo.department,
        position:hipo.position,
        Aspiration: hipo.aspiration,
        Judgement: hipo.judgement,
        Drive: hipo.drive,
        ChangeAgility: hipo.changeAgility,
        PotentialAverage: hipo.potentialAverage,
        PotentialRating: hipo.potentialRating,
        TalentRating: hipo.talentRating,
        NextPotentialRole: hipo.nextPotentialRole,
        PerformanceRatings: hipo.performanceRating.map(pr => `${pr.performanceYear}: ${pr.performanceRating}`).join(', '),
       Interventions: hipo?.interventions?.map(interv => `${interv.developmentInterventions} (${interv.howToAchieve})`).join('; ')
    }));

    // Create a new workbook
    const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(dataToExport);
    const workbook: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'HIPOs Data');

    // Generate file name
    const fileName = 'HIPOs_Data.xlsx';

    // Write workbook and save
    XLSX.writeFile(workbook, fileName);
}

  openDialog(id:number){
    const data = {
      "potentialNextRole": this.mappedSuccession?.get("potentialNextRole")?.value
    }
    if (data.potentialNextRole == null || data.potentialNextRole == "") {
      this.snackBar.open("Potential Next Role is empty", "Close", {duration:3600, verticalPosition:"top"})
    } else {
          // this.dialog.openHiposInterventionsComponent )
       const dialogref:MatDialogRef<HiposInterventionsComponent> =   this.dialog.open(HiposInterventionsComponent ,{
            width: "50vw",
            data:{
             employeeId: id,
             employeeData: data
            }
           })
           dialogref.afterClosed().subscribe(
            ((result) =>{
              this.getHipoData()
            })
           )
    }
    

  }

  getAttScore(attScores: AttScore[], pattName: string): number {
    const scoreObj = attScores.find(score => score.pattName === pattName);
    return scoreObj ? parseInt(scoreObj.score) : 0;  // Default to 0 if not found
  }

  applyFiltertoSeach(event: Event) {
    this.filterText = (event.target as HTMLInputElement).value;
    this.filterSearchData();
  }

  filterSearchData(): void {
    if (this.filterText.trim()) {
      this.allHIPOsData = this.data.filter(item =>
        item.name.toLowerCase().includes(this.filterText.toLowerCase())
      );
    } else {
      this.data = [...this.allHIPOsData]; // Reset to original data if filterText is empty
    }
    this.updatePagination();
  }

  updatePagination(): void {
    this.totalPages = Math.ceil(this.allHIPOsData.length / this.itemsPerPage);
    this.paginate();
  }

  paginate(): void {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    const end = start + this.itemsPerPage;
    this.paginatedHIPOS = this.allHIPOsData.slice(start, end);
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

}
interface Employee {
  userId: string;
  name: string;
  aspiration: number;
  judgement: number;
  drive: number;
  changeAgility: number;
  potentialAverage: number;
  potentialRating: string;
  performanceRating: PerformanceRating[];
  talentRating: string;
  nextPotentialRole: string;
  interventions: Intervention[];
}

interface PerformanceRating {
  performanceRating: number;
  performanceYear: number;
}

interface Intervention {
  developmentInterventions: string;
  howToAchieve: string;
}