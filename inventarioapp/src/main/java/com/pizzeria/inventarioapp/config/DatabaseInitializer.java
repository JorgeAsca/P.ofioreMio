package com.pizzeria.inventarioapp.config;

import com.pizzeria.inventarioapp.Models.Entity.*; //Importa todas las entidades
import com.pizzeria.inventarioapp.Models.Repository.*; //Importa todos los repositorios
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder; // Para codificar contraseñas
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Component // Para que Spring lo detecte como un bean y lo gestione
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SubCateogryRepository subCategoryRepository;
    private final ProductRepository productRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuItemIngredienteRepository menuItemIngredientRepository;
    private final SalesRecordRepository salesRecordRepository; // Opcional para datos iniciales
    private final SoldItemRepository soldItemRepository; // Opcional para datos iniciales
    private final PasswordEncoder passwordEncoder; 

    public DatabaseInitializer(UserRepository userRepository,
            CategoryRepository categoryRepository,
            SubCateogryRepository subCategoryRepository,
            ProductRepository productRepository,
            MenuItemRepository menuItemRepository,
            MenuItemIngredienteRepository menuItemIngredientRepository,
            SalesRecordRepository salesRecordRepository,
            SoldItemRepository soldItemRepository,
            PasswordEncoder passwordEncoder) { // Asegúrate de tener un bean PasswordEncoder configurado
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.productRepository = productRepository;
        this.menuItemRepository = menuItemRepository;
        this.menuItemIngredientRepository = menuItemIngredientRepository;
        this.salesRecordRepository = salesRecordRepository;
        this.soldItemRepository = soldItemRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional 
    public void run(String... args) throws Exception {
        System.out.println("Iniciando la inicialización de la base de datos...");

        // --- 1. Crear Usuarios ---
        if (userRepository.count() == 0) {
            Usuarios adminUser = new Usuarios();
            adminUser.setNombreUsuario("admin");
            adminUser.setPasswordHash(passwordEncoder.encode("admin123")); 
            adminUser.setRole("ADMIN"); 
            userRepository.save(adminUser);
            System.out.println("Usuario administrador creado.");
        }

        // --- 2. Crear Categorías ---
        Categoria catPizza = null, catPlancha = null, catVarios = null;
        if (categoryRepository.count() == 0) {
            catPizza = new Categoria();
            catPizza.setNombreCategoria("Productos de Pizza");
            catPizza = categoryRepository.save(catPizza); // Guardar y reasignar para obtener el ID generado

            catPlancha = new Categoria();
            catPlancha.setNombreCategoria("Productos de Plancha");
            catPlancha = categoryRepository.save(catPlancha);

            catVarios = new Categoria();
            catVarios.setNombreCategoria("Productos Varios");
            catVarios = categoryRepository.save(catVarios);
            System.out.println("Categorías principales creadas.");
        } else {
            // Si ya existen, cárgalas para usarlas después
            catPizza = categoryRepository.findByNombreCategoria("Productos de Pizza").orElse(null);
            catPlancha = categoryRepository.findByNombreCategoria("Productos de Plancha").orElse(null);
            catVarios = categoryRepository.findByNombreCategoria("Productos Varios").orElse(null);
        }

        // --- 3. Crear Subcategorías (para Productos de Pizza) ---
        Subcategoria subDentro = null, subFuera = null;
        if (subCategoryRepository.count() == 0 && catPizza != null) {
            subDentro = new Subcategoria();
            subDentro.setCategoria(catPizza);
            subDentro.setNombreSubcategoria("Dentro del Pueblo");
            subDentro = subCategoryRepository.save(subDentro);

            subFuera = new Subcategoria();
            subFuera.setCategoria(catPizza);
            subFuera.setNombreSubcategoria("Fuera del Pueblo");
            subFuera = subCategoryRepository.save(subFuera);
            System.out.println("Subcategorías para pizza creadas.");
        } else if (catPizza != null) {
            subDentro = subCategoryRepository.findByNombreSubcategoria("Dentro del Pueblo").orElse(null);
            subFuera = subCategoryRepository.findByNombreSubcategoria("Fuera del Pueblo").orElse(null);
        }


        // --- 4. Crear Productos (Ingredientes y Otros) ---
        Productos harina = null, tomate = null, queso = null, pepperoni = null, cocaCola = null;
        if (productRepository.count() == 0) {
            if (catPizza != null) {
                harina = new Productos();
                harina.setNombreProducto("Harina de Trigo");
                harina.setCurrentStock(new BigDecimal("50.00"));
                harina.setUnitOfMeasure("kg");
                harina.setCategoria(catPizza);
                harina.setImagenUrl("https://ejemplo.com/imagenes/harina.jpg");
                // harina.setSubCategory(subDentro); 
                harina = productRepository.save(harina);

                tomate = new Productos();
                tomate.setNombreProducto("Salsa de Tomate");
                tomate.setCurrentStock(new BigDecimal("20.00"));
                tomate.setUnitOfMeasure("litros");
                tomate.setCategoria(catPizza);
                tomate = productRepository.save(tomate);

                queso = new Productos();
                queso.setNombreProducto("Queso Mozzarella");
                queso.setCurrentStock(new BigDecimal("15.00"));
                queso.setUnitOfMeasure("kg");
                queso.setCategoria(catPizza);
                queso = productRepository.save(queso);

                pepperoni = new Productos();
                pepperoni.setNombreProducto("Pepperoni");
                pepperoni.setCurrentStock(new BigDecimal("10.00"));
                pepperoni.setUnitOfMeasure("kg");
                pepperoni.setCategoria(catPizza);
                pepperoni = productRepository.save(pepperoni);
            }

            if (catVarios != null) {
                cocaCola = new Productos();
                cocaCola.setNombreProducto("Coca-Cola 2L");
                cocaCola.setCurrentStock(new BigDecimal("100"));
                cocaCola.setUnitOfMeasure("unidades");
                cocaCola.setCategoria(catVarios);
                cocaCola = productRepository.save(cocaCola);
            }
            System.out.println("Productos de ejemplo creados.");
        } else {
            // Cargar productos si ya existen para usarlos en recetas
            harina = productRepository.findAll().stream().filter(p -> p.getNombreProducto().equals("Harina de Trigo")).findFirst().orElse(null);
            tomate = productRepository.findAll().stream().filter(p -> p.getNombreProducto().equals("Salsa de Tomate")).findFirst().orElse(null);
            queso = productRepository.findAll().stream().filter(p -> p.getNombreProducto().equals("Queso Mozzarella")).findFirst().orElse(null);
            pepperoni = productRepository.findAll().stream().filter(p -> p.getNombreProducto().equals("Pepperoni")).findFirst().orElse(null);
        }


        // --- 5. Crear Ítems del Menú (Pizzas) ---
        Menu pizzaPepperoni = null;
        if (menuItemRepository.count() == 0) {
            pizzaPepperoni = new Menu();
            pizzaPepperoni.setItemName("Pizza Pepperoni");
            pizzaPepperoni.setDescription("Clásica pizza de pepperoni con queso mozzarella.");
            pizzaPepperoni.setSalePrice(new BigDecimal("12.50"));
            pizzaPepperoni.setIsAvailable(true);
            pizzaPepperoni = menuItemRepository.save(pizzaPepperoni);
            System.out.println("Ítems del menú de ejemplo creados.");
        } else {
            pizzaPepperoni = menuItemRepository.findByItemName("Pizza Pepperoni").orElse(null);
        }

        // --- 6. Crear Ingredientes para los Ítems del Menú (Recetas) ---
        if (menuItemIngredientRepository.count() == 0 && pizzaPepperoni != null && harina != null && tomate != null && queso != null && pepperoni != null) {
            MenuItemIngrediente ingHarina = new MenuItemIngrediente();
            ingHarina.setMenu(pizzaPepperoni);
            ingHarina.setProduct(harina);
            ingHarina.setQuantityRequired(new BigDecimal("0.250")); // 250g
            ingHarina.setUnitRequired("kg");
            menuItemIngredientRepository.save(ingHarina);

            MenuItemIngrediente ingTomate = new MenuItemIngrediente();
            ingTomate.setMenu(pizzaPepperoni);
            ingTomate.setProduct(tomate);
            ingTomate.setQuantityRequired(new BigDecimal("0.150")); // 150ml
            ingTomate.setUnitRequired("litros");
            menuItemIngredientRepository.save(ingTomate);

            MenuItemIngrediente ingQueso = new MenuItemIngrediente();
            ingQueso.setMenu(pizzaPepperoni);
            ingQueso.setProduct(queso);
            ingQueso.setQuantityRequired(new BigDecimal("0.200")); // 200g
            ingQueso.setUnitRequired("kg");
            menuItemIngredientRepository.save(ingQueso);

            MenuItemIngrediente ingPepperoni = new MenuItemIngrediente();
            ingPepperoni.setMenu(pizzaPepperoni);
            ingPepperoni.setProduct(pepperoni);
            ingPepperoni.setQuantityRequired(new BigDecimal("0.100")); // 100g
            ingPepperoni.setUnitRequired("kg");
            menuItemIngredientRepository.save(ingPepperoni);
            System.out.println("Receta para Pizza Pepperoni creada.");
        }
        System.out.println("Inicialización de la base de datos completada.");
    }
}