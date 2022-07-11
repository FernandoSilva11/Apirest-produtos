package com.produtos.apirest.resources;

import com.produtos.apirest.models.Produto;
import com.produtos.apirest.repository.ProdutoRepository;
import com.produtos.apirest.responses.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

@CrossOrigin(origins = "https://example.com")
@RestController
@RequestMapping(value="/api")
@Api(value="API REST Produtos.")
public class ProdutoResource {

    @Autowired
    ProdutoRepository produtoRepository;

    @ApiOperation(value = "Cadastra um produto")
    @PostMapping(value="/produto")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Requisição com algum erro"),
            @ApiResponse(code = 500, message = "Um erro inesperado aconteceu")
    })
    @ResponseBody
    public ResponseEntity<Response<Produto>>salvaProduto(@RequestBody Produto produto, BindingResult result) {

        Response<Produto> response = new Response<Produto>();

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        Produto produtoSalvo = produtoRepository.save(produto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(produtoSalvo.getId())
                .toUri();

        response.setData(produtoSalvo);

        return ResponseEntity.created(location).body(response);
    }

    @ApiOperation(value = "Retorna todos os produtos")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/produtos")
    public ResponseEntity<Response<List<Produto>>> listaProdutos(){
        List<Produto> produtos = null;
        produtos = produtoRepository.findAll();

        Response<List<Produto>> produtosResponse = new Response<>();

        produtosResponse.setData(produtos);
        return ResponseEntity.status(HttpStatus.OK).body(produtosResponse);
    }

    @ApiOperation(value="Retorna um produto unico")
    @GetMapping("/produto/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Produto não encontrado"),
            @ApiResponse(code = 500, message = "Um erro inesperado aconteceu")
    })
    public ResponseEntity<Response<Produto>> listaProdutoUnico(@PathVariable(value="id") long id) throws IOException, NotFoundException {
        Response<Produto> response = new Response<Produto>();
        Produto produto;

        try {
            produto = produtoRepository.findById(id);
        } catch (HttpClientErrorException hce) {
            response.setErrors(Collections.singletonList(hce.getStatusText()));
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }
        buscar(produto);
        response.setData(produto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiOperation(value="Atualiza um produto")
    @PutMapping("/produto/{id}")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Produto não encontrado"),
            @ApiResponse(code = 500, message = "Um erro inesperado aconteceu")
    })
    public ResponseEntity<Response<Produto>> atualizaProduto(@PathVariable(value="id") long id, @RequestBody Produto produto) throws NotFoundException {


        Produto produtoexistente = produtoRepository.findById(id);
        if (buscar(produtoexistente) !=null){
            produtoexistente.setNome(produto.getNome());
            produtoexistente.setQuantidade(produto.getQuantidade());
            produtoexistente.setValor(produto.getValor());
            produtoRepository.save(produtoexistente);
        }
        Response<Produto> response = new Response<Produto>();
        response.setData(produto);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @ApiOperation(value = "Apaga um produto específico")
    @DeleteMapping(value="/produto/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Produto deletado com sucesso"),
            @ApiResponse(code = 404, message = "Produto não encontrado"),
            @ApiResponse(code = 500, message = "Um erro inesperado aconteceu")
    })
    public ResponseEntity<Response<Produto>> deletaProduto(@PathVariable(value="id") long id) throws NotFoundException {

        Produto produto = produtoRepository.findById(id);
        if (buscar(produto) !=null) {
            produtoRepository.deleteById(id);
        }
        Response<Produto> response = new Response<>();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    public Produto buscar(Produto produto) throws NotFoundException {
        if (produto == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Produto não encontrado");
        }
        return produto;}
}
