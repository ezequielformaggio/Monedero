package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import dds.monedero.model.Movimiento.Deposito;
import dds.monedero.model.Movimiento.Extraccion;
import dds.monedero.model.Movimiento.Movimiento;
import dds.monedero.model.Movimiento.TipoMovimiento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void poner(double monto) {

    chequearMontoNegativo(monto);

    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }

    agregarMovimiento(LocalDate.now(), monto, new Deposito());

  }

  public void sacar(double monto) {

    chequearMontoNegativo(monto);

    if (getSaldo() - monto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (monto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }

    agregarMovimiento(LocalDate.now(), monto, new Extraccion());
  }

  public void agregarMovimiento(LocalDate fecha, double monto, TipoMovimiento tipo) {
    Movimiento movimiento = new Movimiento(fecha, monto, tipo);
    tipo.impactarEnCuenta(this, monto);
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void extraerSaldo(double monto) {
    this.saldo -= monto;
  }

  public void depositarSaldo(double monto) {
    this.saldo += monto;
  }

  public void chequearMontoNegativo(double monto) {
    if (monto < 0) {
      throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

}
