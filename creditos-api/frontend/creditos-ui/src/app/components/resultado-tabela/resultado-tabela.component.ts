import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

export interface Credito {
  id: string;
  numeroNfse: string;
  numeroCredito: string;
  dataVencimento: Date;
  valor: number;
  status: 'PENDENTE' | 'PAGO' | 'VENCIDO' | 'CANCELADO';
  contribuinte: string;
  descricao: string;
  taxas?: number;
  juros?: number;
  valorTotal?: number;
}

@Component({
  selector: 'app-resultado-tabela',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatIconModule,
    MatButtonModule,
    MatCardModule,
    MatChipsModule,
    MatTooltipModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './resultado-tabela.component.html',
  styleUrls: ['./resultado-tabela.component.scss']
})
export class ResultadoTabelaComponent implements OnChanges {
  @Input() creditos: Credito[] = [];
  @Input() loading = false;
  @Input() erro: string | null = null;

  displayedColumns: string[] = [
    'numeroNfse',
    'numeroCredito',
    'contribuinte',
    'dataVencimento',
    'valor',
    'status',
    'acoes'
  ];

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['creditos'] && this.creditos.length > 0) {
      // Calcular valores totais incluindo taxas e juros
      this.creditos = this.creditos.map(credito => ({
        ...credito,
        valorTotal: this.calcularValorTotal(credito)
      }));
    }
  }

  private calcularValorTotal(credito: Credito): number {
    const taxas = credito.taxas || 0;
    const juros = credito.juros || 0;
    return credito.valor + taxas + juros;
  }

  getStatusColor(status: string): string {
    const colors = {
      'PENDENTE': 'primary',
      'PAGO': 'accent',
      'VENCIDO': 'warn',
      'CANCELADO': ''
    };
    return colors[status as keyof typeof colors] || '';
  }

  getStatusIcon(status: string): string {
    const icons = {
      'PENDENTE': 'schedule',
      'PAGO': 'check_circle',
      'VENCIDO': 'error',
      'CANCELADO': 'cancel'
    };
    return icons[status as keyof typeof icons] || 'help';
  }

  isVencido(dataVencimento: Date): boolean {
    return new Date(dataVencimento) < new Date();
  }

  formatarMoeda(valor: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(valor);
  }

  formatarData(data: Date): string {
    return new Intl.DateTimeFormat('pt-BR').format(new Date(data));
  }

  visualizarDetalhes(credito: Credito): void {
    // Implementar abertura de modal ou navegação para detalhes
    console.log('Visualizar detalhes:', credito);
  }

  baixarComprovante(credito: Credito): void {
    // Implementar download do comprovante
    console.log('Baixar comprovante:', credito);
  }

  exportarDados(): void {
    // Implementar exportação dos dados (CSV, PDF, etc.)
    console.log('Exportar dados:', this.creditos);
  }
}
