@GetMapping("/lista")
public String lista(Model model) {

    model.addAttribute("familias", filaService.listarFila());

    model.addAttribute("totalFila", filaService.listarFila().size());

    return "fila/lista";
}