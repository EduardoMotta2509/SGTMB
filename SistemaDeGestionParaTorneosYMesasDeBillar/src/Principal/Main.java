package Principal;

import Modelos.*;
import EstructurasLineales.*;
import EstructurasNoLineales.*;
import Exceptions.*;

//AVANCE 1:

public class Main {
    public static void main(String[] args) {
    	//    	System.out.println("  SISTEMA DE GESTIÓN - SALÓN DE BILLAR v1.0   "+"\n");
    	//    	System.out.println("1 -> Mostrar Estado de las Mesas"); //Arreglo
    	//    	System.out.println("2 -> Asignar Mesa / Registrar en Lista de Espera"); //Cola
    	//    	System.out.println("3 -> Liberar Mesa y Generar Comprobante"); //Cola
    	//    	System.out.println("4 -> Módulo de Torneos"); //Almacena de forma cronológica inversa las acciones críticas de arbitraje o actualización de puntajes
    	//    	System.out.println("5 -> Buscar Miembro Frecuente");//AVL:A medida que el número de socios del club aumenta, AVL organiza a los usuarios por su id
    	//    	System.out.println("6 -> Salir del Sistema");


    	// 1. ARREGLO ESTÁTICO: Gestión de Mesas
    	System.out.println("--- 1. INICIALIZANDO RECURSOS FÍSICOS (MESAS) ---");
    	Mesa[] salon = new Mesa[3];
    	for (int i = 0; i < salon.length; i++) {
    		salon[i] = new Mesa(i + 1);
    	}
    	for (Mesa m : salon) {
    		System.out.println(m.toString());
    	}

    	// 2. ÁRBOL AVL: Gestión de Miembros
    	System.out.println("\n--- 2. REGISTRANDO MIEMBROS EN ÁRBOL AVL ---");
    	ArbolAVL<String> miembros = new ArbolAVL<>();
    	try {
    		miembros.insert("MBR-005 (Carlos)");
    		miembros.insert("MBR-002 (Ana)");
    		miembros.insert("MBR-008 (Luis)");
    		System.out.println("Miembros insertados. El árbol se auto-balanceó correctamente.");

    		// Demostración de búsqueda rápida
    		System.out.println("Buscando ID 'MBR-002 (Ana)': " + miembros.search("MBR-002 (Ana)"));
    	} catch (ItemDuplicated | ItemNoFound e) {
    		System.out.println("Error AVL: " + e.getMessage());
    	}

    	// 3. LÓGICA DE NEGOCIO: Simulación de Alquiler
    	System.out.println("\n--- 3. SIMULACIÓN DE ALQUILER Y CÁLCULO DE TARIFA ---");
    	try {
    		System.out.println("Cliente ocupa la Mesa 1 a las 14:30...");
    		salon[0].setOcupada(true);
    		salon[0].setHoraInicio("14:30");
    		System.out.println(salon[0].toString());

    		System.out.println("El cliente termina de jugar a las 16:45...");
    		double cobro = salon[0].calcularPrecio("16:45");
    		System.out.printf("Total a cobrar por el tiempo transcurrido: S/ %.2f\n", cobro);

    		// Se resetea la mesa para el próximo cliente
    		salon[0].setOcupada(false);
    		salon[0].setHoraInicio("-");
    	} catch (Exception e) {
    		System.out.println("Error en alquiler: " + e.getMessage());
    	}

    	// 4. LISTA DOBLEMENTE ENLAZADA: Auditoría Financiera
    	System.out.println("\n--- 4. LIBRO DIARIO DE CAJA (LISTA DOBLE) ---");
    	ListaDobleEnlazada<String> libroDiario = new ListaDobleEnlazada<>();
    	libroDiario.insertarAlFinal("TXN-001: Pago Mesa 1 - S/ 27.00");
    	libroDiario.insertarAlFinal("TXN-002: Consumo Bebidas - S/ 15.00");
    	libroDiario.insertarAlFinal("TXN-003: Inscripción Torneo - S/ 50.00");

    	try {
    		System.out.println("Auditoría de Apertura a Cierre (Hacia Adelante):");
    		libroDiario.imprimirHaciaAdelante();

    		System.out.println("\nAuditoría de Cierre a Apertura (Hacia Atrás):");
    		libroDiario.imprimirHaciaAtras();
    	} catch (ExceptionIsEmpty e) {
    		System.out.println("Error Lista Doble: " + e.getMessage());
    	}
    }        
}