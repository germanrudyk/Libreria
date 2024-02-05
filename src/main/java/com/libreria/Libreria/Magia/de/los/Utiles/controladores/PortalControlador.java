/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.libreria.Libreria.Magia.de.los.Utiles.controladores;

import com.libreria.Libreria.Magia.de.los.Utiles.entidades.Producto;
import com.libreria.Libreria.Magia.de.los.Utiles.excepciones.MiException;
import com.libreria.Libreria.Magia.de.los.Utiles.repositorios.ProductoRepositorio;
import com.libreria.Libreria.Magia.de.los.Utiles.servicios.ProductoServicio;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author German
 */
@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private ProductoServicio productoServicio;

    @GetMapping("/")
    public String index(ModelMap modelo) {

        listarProductos(modelo);

        return "index";

    }

    @PostMapping("/agregar")
    public String agregar(@RequestParam String nombre, @RequestParam Integer stock,
            @RequestParam Integer precio, ModelMap modelo) {

        try {
            productoServicio.crearProducto(nombre, stock, precio);
            modelo.put("exito", "Producto agregado correctamente!");

            listarProductos(modelo);

            return "index";
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());

            listarProductos(modelo);

            return "index";

        }

    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, @RequestParam String nombre, @RequestParam Integer stock,
            @RequestParam float porcentaje, ModelMap modelo) {

        try {
            productoServicio.modificarProducto(id, nombre, stock, porcentaje);
            modelo.addAttribute("exito", "Producto modificado correctamente!");

            listarProductos(modelo);

            return "index";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());

            listarProductos(modelo);

            return "index";
        }

    }

    @PostMapping("/vender/{id}")
    public String vender(@PathVariable String id, @RequestParam Integer stock, ModelMap modelo) {

        try {
            productoServicio.venderProducto(id, stock);
            modelo.put("exito", "Producto vendido exitosamente!");

            listarProductos(modelo);

            return "index";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());

            listarProductos(modelo);

            return "index";

        }

    }

    @GetMapping("/faltantes")
    public String faltantes(ModelMap modelo) {

        List<Producto> productos = productoServicio.listarFaltantes();

        modelo.addAttribute("productos", productos);

        return "faltantes";

    }

    @GetMapping("/precios")
    public String precios(ModelMap modelo) {

        List<Producto> productos = productoServicio.listarProductos();

        modelo.addAttribute("productos", productos);

        return "precios";

    }

    @PostMapping("/aplicar")
    public String aplicar(@RequestParam String[] id, @RequestParam float porcentaje, ModelMap modelo) {

        productoServicio.aplicarPrecios(id, porcentaje);

        modelo.put("exito", "Precios modificados correctamente!");

        listarProductos(modelo);

        return "index";
    }
    
    private void listarProductos(ModelMap modelo){
        
        List<Producto> productos = productoServicio.listarProductos();

        modelo.addAttribute("productos", productos);

        List<Producto> productosFaltantes = productoServicio.listarFaltantes();

        productoServicio.establecerFecha(productosFaltantes);

        List<Producto> productosANotificar = productoServicio.notificar(productosFaltantes);

        modelo.put("productosANotificar", productosANotificar);
        
    }

}
