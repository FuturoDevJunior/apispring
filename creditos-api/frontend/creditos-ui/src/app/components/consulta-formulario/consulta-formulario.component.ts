import { Component, Output, EventEmitter, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatRadioModule } from '@angular/material/radio';
import { MatIconModule } from '@angular/material/icon';

export interface ConsultaFormData {
  tipoConsulta: 'nfse' | 'credito';
  valor: string;
}

@Component({
  selector: 'app-consulta-formulario',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatRadioModule,
    MatIconModule
  ],
  template: `
    <mat-card class="consulta-card">
      <mat-card-header>
        <mat-card-title>
          <mat-icon>search</mat-icon>
          Consultar Créditos
        </mat-card-title>
      </mat-card-header>
      
      <mat-card-content>
        <form [formGroup]="consultaForm" (ngSubmit)="onSubmit()">
          <div class="form-group">
            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Tipo de Consulta</mat-label>
              <mat-radio-group formControlName="tipoConsulta" class="radio-group">
                <mat-radio-button value="nfse">Número da NFS-e</mat-radio-button>
                <mat-radio-button value="credito">Número do Crédito</mat-radio-button>
              </mat-radio-group>
            </mat-form-field>
          </div>
          
          <div class="form-group">
            <mat-form-field appearance="outline" class="full-width">
              <mat-label>{{ getLabelForInput() }}</mat-label>
              <input 
                matInput 
                formControlName="valor" 
                [placeholder]="getPlaceholderForInput()"
                maxlength="50">
              <mat-icon matSuffix>{{ getIconForInput() }}</mat-icon>
              <mat-error *ngIf="consultaForm.get('valor')?.hasError('required')">
                {{ getLabelForInput() }} é obrigatório
              </mat-error>
              <mat-error *ngIf="consultaForm.get('valor')?.hasError('minlength')">
                {{ getLabelForInput() }} deve ter pelo menos 3 caracteres
              </mat-error>
            </mat-form-field>
          </div>
          
          <div class="form-actions">
            <button 
              mat-raised-button 
              color="primary" 
              type="submit" 
              [disabled]="consultaForm.invalid || loading"
              class="consultar-btn">
              <mat-icon>search</mat-icon>
              {{ loading ? 'Consultando...' : 'Consultar' }}
            </button>
            
            <button 
              mat-button 
              type="button" 
              (click)="limpar()"
              [disabled]="loading"
              class="limpar-btn">
              <mat-icon>clear</mat-icon>
              Limpar
            </button>
          </div>
        </form>
      </mat-card-content>
    </mat-card>
  `,
  styleUrls: ['./consulta-formulario.component.scss']
})
export class ConsultaFormularioComponent {
  @Output() consultar = new EventEmitter<ConsultaFormData>();
  
  consultaForm: FormGroup;
  loading = false;

  private fb = inject(FormBuilder);

  constructor() {
    this.consultaForm = this.fb.group({
      tipoConsulta: ['nfse', Validators.required],
      valor: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  onSubmit(): void {
    if (this.consultaForm.valid) {
      const formData: ConsultaFormData = {
        tipoConsulta: this.consultaForm.get('tipoConsulta')?.value,
        valor: this.consultaForm.get('valor')?.value.trim()
      };
      
      this.consultar.emit(formData);
    }
  }

  limpar(): void {
    this.consultaForm.patchValue({
      tipoConsulta: 'nfse',
      valor: ''
    });
  }

  setLoading(loading: boolean): void {
    this.loading = loading;
  }

  getLabelForInput(): string {
    const tipoConsulta = this.consultaForm.get('tipoConsulta')?.value;
    return tipoConsulta === 'nfse' ? 'Número da NFS-e' : 'Número do Crédito';
  }

  getPlaceholderForInput(): string {
    const tipoConsulta = this.consultaForm.get('tipoConsulta')?.value;
    return tipoConsulta === 'nfse' ? 'Ex: 7891011' : 'Ex: 123456';
  }

  getIconForInput(): string {
    const tipoConsulta = this.consultaForm.get('tipoConsulta')?.value;
    return tipoConsulta === 'nfse' ? 'receipt' : 'credit_card';
  }
}
