/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libreria.Libreria.Magia.de.los.Utiles.servicios;

import com.libreria.Libreria.Magia.de.los.Utiles.entidades.Producto;
import com.libreria.Libreria.Magia.de.los.Utiles.excepciones.MiException;
import com.libreria.Libreria.Magia.de.los.Utiles.repositorios.ProductoRepositorio;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author German
 */
@Service
public class ProductoServicio {

    @Autowired
    private ProductoRepositorio productoRepositorio;

    public void crearProducto(String nombre, Integer stock, Integer precio) throws MiException {

        validar(nombre, stock, precio);

        Producto producto = new Producto();

        producto.setNombre(nombre);
        producto.setStock(stock);
        producto.setPrecio(precio);

        producto.setNotificada(Boolean.FALSE);

        productoRepositorio.save(producto);

    }

    private void validar(String nombre, Integer stock, Integer precio) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("Ingrese nombre del producto");
        }
        if (stock == null) {
            throw new MiException("Ingrese stock disponible");
        }
        if (precio == null) {
            throw new MiException("Ingrese el precio");
        }

    }

    public void modificarProducto(String id, String nombre, Integer stock, double porcentaje) throws MiException {

        Producto producto = buscarProductoPorId(id);

        if (producto != null) {

            producto.setNombre(nombre);
            producto.setStock(stock);

            aplicarPorcentaje(producto, porcentaje);

            if (producto.getStock() > 2) {
                producto.setFechaNotificada(null);
                producto.setNotificada(Boolean.FALSE);
            }

            productoRepositorio.save(producto);

        }
    }

    public Producto buscarProductoPorId(String id) {

        Optional<Producto> respuesta = productoRepositorio.findById(id);

        System.out.println(respuesta.get().getId());

        if (respuesta.isPresent()) {

            return respuesta.get();

        }

        return null;

    }

    public void venderProducto(String id, Integer stock) throws MiException {

        Producto producto = buscarProductoPorId(id);

        if (producto != null) {

            if ((producto.getStock() - stock) >= 0) {

                producto.setStock(producto.getStock() - stock);

                productoRepositorio.save(producto);

            } else {

                throw new MiException("No se dispone de esa cantidad para vender");

            }

        }

    }

    public List<Producto> listarProductos() {

        return productoRepositorio.listarTodosLosProductos();

    }

    public List<Producto> listarFaltantes() {

        return productoRepositorio.listarFaltantes();

    }

    public void establecerFecha(List<Producto> productos) {

        for (Producto producto : productos) {
            if (producto.getFechaNotificada() == null) {
                producto.setFechaNotificada(LocalDate.now());
                System.out.println(producto.getFechaNotificada());
                productoRepositorio.save(producto);
            }
        }

    }

    public List<Producto> notificar(List<Producto> productos) {

        List<Producto> productosANotificar = new ArrayList();

        for (Producto producto : productos) {

            int comparacion = LocalDate.now().compareTo(producto.getFechaNotificada());

            if (comparacion > 0 && !(producto.getNotificada())) {
                producto.setNotificada(Boolean.TRUE);
                productoRepositorio.save(producto);
                productosANotificar.add(producto);
                System.out.println(producto.getNombre());
            }
        }

        return productosANotificar;

    }

    public void aplicarPrecios(String[] id, double porcentaje) {

        for (String aux : id) {

            Producto producto = buscarProductoPorId(aux);
            
            aplicarPorcentaje(producto, porcentaje);

            productoRepositorio.save(producto);
        }

    }

    private void aplicarPorcentaje(Producto producto, double porcentaje) {

        double porcentajeAAplicar = (porcentaje / 100) + 1;
        
        double precio = producto.getPrecio() * porcentajeAAplicar;

        int precioAplicado = (int) Math.round(precio / 10) * 10;

        producto.setPrecio(precioAplicado);

    }
}
