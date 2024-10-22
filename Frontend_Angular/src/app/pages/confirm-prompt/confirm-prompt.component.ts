import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-confirm-prompt',
  templateUrl: './confirm-prompt.component.html',
  styleUrl: './confirm-prompt.component.scss'
})
export class ConfirmPromptComponent {

  constructor(
    public dialogRef: MatDialogRef<ConfirmPromptComponent>,
    @Inject(MAT_DIALOG_DATA) public message: string
  ) {}

  // Close dialog with confirmation (true)
  onConfirm(): void {
    this.dialogRef.close(true);
  }

  // Close dialog with cancellation (false)
  onCancel(): void {
    console.log(this.message);
    
    this.dialogRef.close(false);
  }
}
