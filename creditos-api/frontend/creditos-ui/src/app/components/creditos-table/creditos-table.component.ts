import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { CreditoDTO } from '../../services/consulta.service';

@Component({
  selector: 'app-creditos-table',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatPaginatorModule,
    MatSortModule
  ],
  template: `
    <mat-card class="resultados-card" *ngIf="creditos.length > 0 || carregando || erro">
      <mat-card-header>
        <mat-card-title>
          <mat-icon>list_alt</mat-icon>
          Resultados da Consulta
        </mat-card-title>
        <mat-card-subtitle *ngIf="!carregando && !erro">
          {{ total }} crédito(s) encontrado(s) • Consultado em {{ dataConsulta | date:'dd/MM/yyyy HH:mm:ss' }}
        </mat-card-subtitle>
      </mat-card-header>

      <mat-card-content>
        <!-- Loading State -->
        <div *ngIf="carregando" class="loading-container">
          <mat-spinner diameter="50"></mat-spinner>
          <p>Consultando créditos...</p>
        </div>

        <!-- Error State -->
        <div *ngIf="erro && !carregando" class="error-container">
          <mat-icon color="warn" class="error-icon">error</mat-icon>
          <h3>Erro na consulta</h3>
          <p>{{ erro }}</p>
          <button mat-raised-button color="primary" (click)="onTentarNovamente()">
            <mat-icon>refresh</mat-icon>
            Tentar Novamente
          </button>
        </div>

        <!-- Empty State -->
        <div *ngIf="!carregando && !erro && creditos.length === 0" class="empty-container">
          <mat-icon class="empty-icon">search_off</mat-icon>
          <h3>Nenhum crédito encontrado</h3>
          <p>Verifique os dados informados e tente novamente.</p>
        </div>

        <!-- Data Table -->
        <div *ngIf="!carregando && !erro && creditos.length > 0" class="table-container">
          <table mat-table [dataSource]="paginatedData" class="creditos-table" matSort>
            
            <!-- Número do Crédito -->
            <ng-container matColumnDef="numeroCredito">
              <th mat-header-cell *matHeaderCellDef mat-sort-header>
                <mat-icon>account_balance</mat-icon>
                Número do Crédito
              </th>
              <td mat-cell *matCellDef="let credito" class="numero-credito">
                {{ credito.numeroCredito }}
              </td>
            </ng-container>

            <!-- CPF/CNPJ -->
            <ng-container matColumnDef="cpfCnpj">
              <th mat-header-cell *matHeaderCellDef mat-sort-header>
                <mat-icon>person</mat-icon>
                CPF/CNPJ
              </th>
              <td mat-cell *matCellDef="let credito">
                {{ formatarCpfCnpj(credito.cpfCnpj) }}
              </td>
            </ng-container>

            <!-- Valor -->
            <ng-container matColumnDef="valor">
              <th mat-header-cell *matHeaderCellDef mat-sort-header>
                <mat-icon>attach_money</mat-icon>
                Valor
              </th>
              <td mat-cell *matCellDef="let credito" class="valor">
                {{ formatarMoeda(credito.valor) }}
              </td>
            </ng-container>

            <!-- Data de Vencimento -->
            <ng-container matColumnDef="dataVencimento">
              <th mat-header-cell *matHeaderCellDef mat-sort-header>
                <mat-icon>event</mat-icon>
                Vencimento
              </th>
              <td mat-cell *matCellDef="let credito">
                {{ credito.dataVencimento | date:'dd/MM/yyyy' }}
              </td>
            </ng-container>

            <!-- Situação -->
            <ng-container matColumnDef="situacao">
              <th mat-header-cell *matHeaderCellDef mat-sort-header>
                <mat-icon>flag</mat-icon>
                Situação
              </th>
              <td mat-cell *matCellDef="let credito">
                <mat-chip 
                  [color]="getCorSituacao(credito.situacao)"
                  class="situacao-chip">
                  {{ credito.situacao }}
                </mat-chip>
              </td>
            </ng-container>

            <!-- Número NFSe -->
            <ng-container matColumnDef="numeroNfse">
              <th mat-header-cell *matHeaderCellDef mat-sort-header>
                <mat-icon>receipt</mat-icon>
                NFSe
              </th>
              <td mat-cell *matCellDef="let credito">
                {{ credito.numeroNfse || '-' }}
              </td>
            </ng-container>

            <!-- Ações -->
            <ng-container matColumnDef="acoes">
              <th mat-header-cell *matHeaderCellDef>Ações</th>
              <td mat-cell *matCellDef="let credito">
                <button 
                  mat-icon-button 
                  color="primary"
                  [title]="'Detalhes do crédito ' + credito.numeroCredito"
                  (click)="onVerDetalhes(credito)">
                  <mat-icon>visibility</mat-icon>
                </button>
              </td>
            </ng-container>

            <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
            <tr mat-row *matRowDef="let row; columns: displayedColumns;" 
                class="credito-row"
                [class.row-vencido]="isVencido(row.dataVencimento)"></tr>
          </table>

          <!-- Paginação -->
          <mat-paginator 
            *ngIf="creditos.length > pageSize"
            [length]="creditos.length"
            [pageSize]="pageSize"
            [pageSizeOptions]="[5, 10, 25, 50]"
            [showFirstLastButtons]="true"
            (page)="onPageChange($event)"
            class="paginator">
          </mat-paginator>
        </div>
      </mat-card-content>
    </mat-card>
  `,
  styles: [`
    .resultados-card {
      margin: 20px auto;
      max-width: 1200px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }

    mat-card-title {
      display: flex;
      align-items: center;
      gap: 8px;
      color: #1976d2;
    }

    .loading-container,
    .error-container,
    .empty-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 40px 20px;
      text-align: center;
    }

    .error-icon,
    .empty-icon {
      font-size: 64px;
      width: 64px;
      height: 64px;
      margin-bottom: 16px;
      color: #666;
    }

    .error-icon {
      color: #f44336;
    }

    .table-container {
      overflow-x: auto;
      margin-top: 16px;
    }

    .creditos-table {
      width: 100%;
      min-width: 800px;
    }

    .mat-mdc-header-cell {
      font-weight: 600;
      color: #1976d2;
    }

    .mat-mdc-header-cell mat-icon {
      margin-right: 4px;
      vertical-align: middle;
    }

    .numero-credito {
      font-family: 'Courier New', monospace;
      font-weight: 500;
    }

    .valor {
      font-weight: 600;
      color: #2e7d32;
    }

    .situacao-chip {
      font-size: 12px;
      font-weight: 500;
      min-height: 24px;
    }

    .credito-row {
      cursor: pointer;
      transition: background-color 0.2s;
    }

    .credito-row:hover {
      background-color: #f5f5f5;
    }

    .row-vencido {
      background-color: #fff3e0;
    }

    .row-vencido:hover {
      background-color: #ffe0b2;
    }

    .paginator {
      margin-top: 16px;
      border-top: 1px solid #e0e0e0;
      padding-top: 16px;
    }

    /* Responsividade */
    @media (max-width: 768px) {
      .resultados-card {
        margin: 10px;
        max-width: none;
      }

      .creditos-table {
        min-width: 600px;
      }

      .mat-mdc-header-cell,
      .mat-mdc-cell {
        padding: 8px 4px;
        font-size: 12px;
      }
    }
  `]
})
export class CreditosTableComponent implements OnChanges {
  @Input() creditos: CreditoDTO[] = [];
  @Input() total = 0;
  @Input() dataConsulta: Date | null = null;
  @Input() carregando = false;
  @Input() erro: string | null = null;

  displayedColumns: string[] = [
    'numeroCredito', 
    'cpfCnpj', 
    'valor', 
    'dataVencimento', 
    'situacao', 
    'numeroNfse', 
    'acoes'
  ];

  paginatedData: CreditoDTO[] = [];
  pageSize = 10;
  currentPage = 0;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['creditos'] && this.creditos) {
      this.updatePaginatedData();
    }
  }

  onPageChange(event: PageEvent): void {
    this.pageSize = event.pageSize;
    this.currentPage = event.pageIndex;
    this.updatePaginatedData();
  }

  private updatePaginatedData(): void {
    const startIndex = this.currentPage * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.paginatedData = this.creditos.slice(startIndex, endIndex);
  }

  formatarCpfCnpj(documento: string): string {
    if (!documento) return '-';
    
    const numbers = documento.replace(/\D/g, '');
    
    if (numbers.length === 11) {
      // CPF
      return numbers.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
    } else if (numbers.length === 14) {
      // CNPJ
      return numbers.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, '$1.$2.$3/$4-$5');
    }
    
    return documento;
  }

  formatarMoeda(valor: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(valor);
  }

  getCorSituacao(situacao: string): 'primary' | 'accent' | 'warn' {
    switch (situacao?.toLowerCase()) {
      case 'pago':
        return 'primary';
      case 'pendente':
        return 'accent';
      case 'vencido':
        return 'warn';
      default:
        return 'accent';
    }
  }

  isVencido(dataVencimento: string): boolean {
    if (!dataVencimento) return false;
    const hoje = new Date();
    const vencimento = new Date(dataVencimento);
    return vencimento < hoje;
  }

  onVerDetalhes(credito: CreditoDTO): void {
    // Implementar modal ou navegação para detalhes
    console.log('Ver detalhes do crédito:', credito);
  }

  onTentarNovamente(): void {
    // Emit event para tentar novamente a consulta
    console.log('Tentando novamente...');
  }
}
