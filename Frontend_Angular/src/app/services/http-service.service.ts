import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { url } from 'node:inspector';
import { Url } from 'node:url';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class HttpServiceService {


  constructor(private http: HttpClient) { }

  // serverUrl: string = 'http://192.168.88.199:8080/v1/api/';
  //serverUrl: string = 'http://192.168.90.127:8080/v1/api/';

  // serverUrl: string = 'http://192.168.2.209:8080';
  // serverUrl: string = 'http://192.168.88.163:8080';
  //  serverUrl: string = 'http://192.168.2.21:8080';


  // serverUrl: string = 'http://192.168.89.11:8080';
 // serverUrl: string = 'http://192.168.89.11:8080';

  // serverUrl
  //serverUrl: string = 'http://localhost:8080/v1/api/';

  // serverUrl: string = 'http://192.168.89.134:8080/v1/api/';
  //serverUrl: string = 'http://192.168.89.134:8080/v1/api/';

  serverUrl: string = 'http://192.168.90.145:8080/v1/api/';

  //serverUrl: string = environment.API_BASE_URL;
  //  serverUrl: string = 'http://192.168.2.21:8080';


  //add RolesFor assesssment
  //add data in general
  postData(url: string, data: any): Observable<any> {
    return this.http.post<any>(url, data);
  }
  getData(url:string): Observable<any>{
    return this.http.get<any>(url);
  }

  createRoleAssessment(managerId: number, formData: any): Observable<any> {
    //http://192.168.100.2:8080/v1/criticalRoles/create/17
    const url = `${this.serverUrl}criticalRoles/create/${managerId}`
    const headers = new HttpHeaders({ 'content-type': 'application/json' })
    return this.http.post<any>(url, formData)

  }
  //get critical roles
  //http://192.168.2.21:8080/v1/criticalRoles/getAllCriticalRoles?managerId=1
  getCriticalRoles(managerId: number): Observable<any> {
    const url = `${this.serverUrl}criticalRoles/getAllCriticalRoles?managerId=${managerId}`
    return this.http.get<any>(url);
  }

  // get currentRoleHolder
  // http://192.168.89.24:8080/v1/api/manager/employees?managerId=7
   getCurrentHolder(managerId: number): Observable<any>{
      const url = `${this.serverUrl}manager/employees?managerId=${managerId}`
      return this.http.get<any>(url);
    }
  //get critical Role by id
  //http://192.168.2.21:8080/v1/criticalRoles/getAssessmentRoleById?roleId=3
  getCriticalRoleByID(roleId: number): Observable<any> {
    const url = `${this.serverUrl}criticalRoles/getAssessmentRoleById?roleId=${roleId}`
    return this.http.get<any>(url);
  }

  // get drivers
  // http://192.168.89.24:8080/V1/api/successionDrivers
  getDrivers(): Observable<any>{
    const url = `${this.serverUrl}successionDrivers`
    return this.http.get<any>(url);
  }
  
  //critical roles edit
  //http://192.168.2.21:8080/v1/criticalRoles/edit/67/123
  criticalRolesEdit(managerId: number, criticalRoleId: number, data: any): Observable<any> {
    const url = `${this.serverUrl}criticalRoles/edit/${criticalRoleId}/${managerId}`
    return this.http.put<any>(url, data);
  }
  //adding strategies
  //http://192.168.2.21:8080/v1/criticalRoles/233/add-strategy/678
  addStrategiesToRole(rolesAssessmentId: number, ManagerId: number, data: any): Observable<any> {
    const url = `${this.serverUrl}criticalRoles/${rolesAssessmentId}/add-strategy/${ManagerId}`
    return this.http.post<any>(url, data)
  }

  //adding attributes for assessment
  createAssessmentAttributes(managerId: number, data: any): Observable<any> {
    const url = `${this.serverUrl}addAttributeList?managerId=${managerId}`
    console.log(url);
    // const headers = new HttpHeaders({ 'content-type': 'application/json' })
    return this.http.post<any>(url, data)
  }

  //adding Interventions for HIPOs
  //http://192.168.88.212:8080/v1/hipos-interventions/create/3
 
  // adding departments http://192.168.88.236:8080/v1/api/departments/addDepartment
  createDepartment(data:any) : Observable<any>{
    const url = `${this.serverUrl}departments/addDepartment`
    return this.http.post <any>(url,data)

  }

  // getting departments
  //http://192.168.88.236:8080/v1/api/departments/getAllDepartments
  getDepartments():Observable<any>{
    const url = `${this.serverUrl}departments/getAllDepartments`
    return this.http.get<any>(url)
  }


 //adding Interventions for HIPOs
 //http://192.168.88.212:8080/v1/hipos-interventions/create/3
 createDevelopmentInterventions(employeeId: number, data: any): Observable<any> {
  const url = `${this.serverUrl}hipos/interventions/create/${employeeId}`;
  return this.http.post<any>(url, data);
}
//potential next roles 
//http:192.168.88.212:8080/v1/hipos/potential_next_roles/create/5
createPotentialNextRole(employeeId: number, data: any): Observable<any> {
  const url = `${this.serverUrl}hipos/potential_next_roles/create/${employeeId}`;
  return this.http.post<any>(url, data);
}
  // adding assessment
  // createAssessment(AttributeId: number, data: any): Observable<any> {
  //   const url = `${this.serverUrl}addAssessments?attributeId=${AttributeId}`
  //   const headers = new HttpHeaders({ 'content-type': 'application/json' })
  //   return this.http.post<any>(url, data)
  // }
  //http://localhost:8080/addAQuestionList?assId=1
  //Adding assessment and questions
  createAssessmentQuestions(assId: number, data: any): Observable<any> {
    const url = `${this.serverUrl}addAQuestionList?assId=${assId}`
    const headers = new HttpHeaders({ 'content-type': 'application/json' })
    return this.http.post<any>(url, data)
  }

  //get assessments, atrributes and assesment questions
  //http://localhost:8080/getAssessments?managerId=1
  getAssessments(managerId: number): Observable<any> {
    const url = `${this.serverUrl}getAssessments?managerId=${managerId}`
    return this.http.get<any>(url);
  }

  //get all attributes
  //http://192.168.2.21:8080/get/all/attributes
  getAssessmentAttributes(): Observable<any> {
    const url = `${this.serverUrl}get/all/attributes`;
    return this.http.get<any>(url);
  }

  // adding critical skills
  //http://localhost:8080/user/create/addSkill?managerId=7


  createCriticalSkills(managerId: any, items: any): Observable<any> {
    const url = `${this.serverUrl}addSkill?managerId=${managerId}`;
    const headers = new HttpHeaders({ 'content-type': 'application/json' })
    return this.http.post<any>(url, items);
  }

  // getting  critical skills
  //http://192.168.100.2:8080/v1/criticalSkills/getAddedSkills?managerId=1

  getCriticalSkills(managerId: number): Observable<any> {
    const url = `${this.serverUrl}criticalSkills/getAddedSkills?managerId=${managerId}`
    return this.http.get<any>(url);
  }

  //getting criticalskill
  //http://192.168.2.21:8080/v1/criticalSkills/getAssessmentSkillById?skillId=12

  getCriticalSkill(skillId: number): Observable<any> {
    const url = `${this.serverUrl}criticalSkills/getAssessmentSkillById?skillId=${skillId}`
    return this.http.get<any>(url);
  }

  // updating critical skill
  //http://192.168.2.21:8080/v1/criticalSkills/8/7
  //http://192.168.88.199:8080/v1/api/criticalSkills/90/1


  updateCriticalSkill(managerId: number, skillId: number, data: any): Observable<any> {

    console.log('sssssssss',data);
    
    const url = `${this.serverUrl}criticalSkills/${skillId}/${managerId}`;
    const headers = new HttpHeaders({ 'content-type': 'application/json' });
    return this.http.put<any>(url, data)

  }


  // create roles
  //http://localhost:8080/createRoles
  createSysRoles(body: any): Observable<any> {
    const url = `${this.serverUrl}createRoles`
    return this.http.post<any>(url, body)
  }

  //http://localhost:8080/generate/criticalRolesReport/excel
  //critical roles report
  getCriticalRoleReport(type: string): Observable<any> {
    const url = `${this.serverUrl}generate/criticalRolesReport/${type}`
    return this.http.get(url, { responseType: 'blob' })
  }
  // getEmployees
  //http://192.168.2.21:8080/v1/performances/HIPOs/all/employees/4
  getEmployees(managerId: number): Observable<any> {
    const url = `${this.serverUrl}performances/HIPOs/all/employees/${managerId}`
    return this.http.get<any>(url)
  }
  //getEmployees
  //http://192.168.2.21:8080/v1/performances/HIPOs/all/employees/4/2024

  //talent map
  //http://192.168.2.21:8080/v1/performances/HIPOs/all/employees/4


  getTalent(managerId: number): Observable<any> {
    const url = `${this.serverUrl}performances/HIPOs/all/employees/${managerId}`
    return this.http.get<any>(url)
  }

  //http://localhost:8080/employees?managerId=1
  //
  getAllEmployees(managerId: string): Observable<any> {
    const url = `${this.serverUrl}manager/employees?managerId=${managerId}`
    return this.http.get(url);
  }
  //http://localhost:8080/v1/api/users/get/all_employees
  getAllUsers(): Observable<any> {
    const url = `${this.serverUrl}users/get/all_employees`
    return this.http.get(url);
  }
  //hipos
  getHipos(url: string): Observable<any> {
    return this.http.get(url);
  }
  //get employee performances for the last three years
  getThreeYearsEmpPerformances(managerId: number): Observable<any> {

    const url = `${this.serverUrl}performances/employees/${managerId}`
    return this.http.get(url);
  }
//get user for token interceptor
getUser(){
 const systemUser = localStorage.getItem('user')
      const perseData = JSON.parse(systemUser)
      const authToken = perseData.authToken
      return authToken || ''
}

uploadEmployees(formData, managerId): Observable<any> {
  const url = `${this.serverUrl}users/upload-employees/${managerId}`;
  return this.http.post(url, formData);
}

getEmployeeById(userId): Observable<any>{
  const url = `${this.serverUrl}manager/employee/byId/?employeeId=${userId}`;
  return this.http.get(url);
}

//http://192.168.89.54:8080/v1/api/succession-plans
//succession planning
createSuccession(body:any):Observable<any> {
 const url = `${this.serverUrl}succession-plans`
 return this.http.post<any>(url , body)
}

//http://192.168.89.24:8080/v1/api/departments/getDepartmentPositions?depId=1
//getting department by id
getDepartment(departId:number):Observable<any> {
   const url = `${this.serverUrl}departments/getDepartmentPositions?depId=${departId}`
   return this.http.get<any>(url )
}

//get questions by attribute id
getQuestionsByAttribute(attrId: number):Observable<any> {
  const url = `${this.serverUrl}getQuestionsByAttributeId?attributeId=${attrId}`;
  return this.http.get<any>(url);
}

//get not done asesments
getNotDoneAssesments(userId:number):Observable<any>{
  const url = `${this.serverUrl}active/not-attempted/${userId}`
  return this.http.get<any>(url);
}

getActiveAssessments():Observable<any>{
  //http://localhost:8080/v1/api/activeAssessments

  const url = `${this.serverUrl}activeAssessments`;
  return this.http.get(url)
}

// submitUserAnswers
postUsersAnswers(data:any): Observable<any>{
  //http://localhost:8080/v1/api/answers/user
  const url =`${this.serverUrl}answers/user`
  return this.http.post<any>(url, data)
}

postManagerAnswers(data:any):Observable<any>{
  const url = `${this.serverUrl}answers/manager`
  return this.http.post<any>(url, data)
}

getAllAssesements():Observable<any>{
  // http://192.168.90.127:8080/v1/api/allAssessments

  const url =`${this.serverUrl}allAssessments`
  return this.http.get<any>(url)
}

postAssesement(data:any, userId:number): Observable<any>{
  // http://192.168.90.127:8080/v1/api/addAssignment

  const url =`${this.serverUrl}addAssignment?managerId=${userId}`
  return this.http.post<any>(url,data)
}

postSuccessionDrive(data:any): Observable<any>{
// http://localhost:8080/v1/api/successionDrivers

const url =`${this.serverUrl}successionDrivers`
return this.http.post<any>(url,data)
}


}

