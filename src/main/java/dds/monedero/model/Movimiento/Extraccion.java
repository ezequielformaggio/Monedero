package dds.monedero.model.Movimiento;

import dds.monedero.model.Cuenta;

public class Extraccion implements TipoMovimiento {

  public void impactarEnCuenta(Cuenta cuenta, double monto) {
    cuenta.extraerSaldo(monto);
  }
}
