import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { HttpServiceService } from '../../../services/http-service.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-talent',
  templateUrl: './talent.component.html',
  styleUrls: ['./talent.component.scss']
})
export class TalentComponent implements OnInit, AfterViewInit {

  constructor(private http: HttpServiceService,    
    private route: ActivatedRoute,
    private router: Router,) {}

  talents: any[] = [];
  rating:string ='';
  managerId: any;
  filterValue: string = '';
  dataSource = new MatTableDataSource<any>([]);
  displayedColumns: string[] = ['index', 'userFullName', 'averagePerformance', 'talentRating', 'manAssessmentAvg', 'talentMapping'];

  @ViewChild(MatPaginator) paginator: MatPaginator;

  ngOnInit() {
    const user = localStorage.getItem("user");
    this.managerId = JSON.parse(user);
    console.log("manager", this.managerId);
    this.route.paramMap.subscribe(params => {
      this.rating = params.get('rating');
    });
    this.displayTalent(this.rating);
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  displayTalent(rating) {
    this.http.getTalent(this.managerId.user.userId).subscribe(
      (res) => {

        this.talents = res.items.filter((item) => item.talentRating === rating );
        console.log('new', this.talents);
        this.dataSource.data = this.talents;
        this.filterTalents(); // Initialize filteredTalents after fetching data
      },
      (err) => {
        console.log('error', err); 
      }
    );
  }

  filterTalents() {
    const filter = this.filterValue.toLowerCase();
    this.dataSource.data = this.talents.filter(talent =>
      talent.userFullName.toLowerCase().includes(filter) ||
      talent.averagePerformance.toString().toLowerCase().includes(filter) ||
      talent.talentRating.toLowerCase().includes(filter) ||
      talent.manAssessmentAvg.toString().toLowerCase().includes(filter) ||
      this.getTalentMapping(talent.talentRating).toLowerCase().includes(filter)
    );
  }

  getTalentMapping(talentRating: string): string {
    return talentRating === 'A2' ? 'Rising Star'
      : talentRating === 'A1' ? 'Proven Star'
      : talentRating === 'A3' ? 'Rough diamond' 
      : talentRating === 'B1' ? 'Current star' 
      : talentRating === 'B2' ? 'Key player'
      : talentRating === 'B3' ? 'Inconsistent Performer' 
      : talentRating === 'C1' ? 'Critical contributor' 
      : talentRating === 'C2' ? 'Solid professional' : 'Poor performer';
  }
}
