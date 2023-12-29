package cl.camanchaca.biomass.adapter.postgres.product;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductDataRepository extends ReactiveCrudRepository<ProductData, Integer> {
    @Query("SELECT * FROM products WHERE codigo IN (:ids)")
    Flux<ProductData> findAllByCodigoList(List<Integer> ids);

    @Query("SELECT * FROM products OFFSET :offset LIMIT :size")
    Flux<ProductData> findAllByOffsetAndSize(@Param("offset") Integer offset, @Param("size") Integer size);

    @Query("INSERT INTO products " +
            "(anomes, codigo, ajuste_sp, ajuste_sp_pr, bloques_bp, " +
            "bloques_harasu, c_dist, c_emp, c_proc_total, caja, calibre, " +
            "calibre_trimd, calidad, calidad_last, calidad_obj, color, degr_wfe, " +
            "descripcion, emp_primario, estado, especie, factor_sp, familia, forma, " +
            "harina, mandt, mesano, neto_wfe, rendimiento, scale, scrape_meat, skin, " +
            "sobrepeso, subp_wfe, target, tipo, ult_per_prod, wfe_bk_bp, wfe_bk_harasu, " +
            "wfe_harina, wfe_meat) " +
            "VALUES (:#{#product.anomes}, :#{#product.codigo}, :#{#product.ajusteSp}, " +
            ":#{#product.ajusteSpPr}, :#{#product.bloquesBp}, :#{#product.bloquesHarasu}, " +
            ":#{#product.cDist}, :#{#product.cEmp}, :#{#product.cProcTotal}, :#{#product.caja}, " +
            ":#{#product.calibre}, :#{#product.calibreTrimd}, :#{#product.calidad}, " +
            ":#{#product.calidadLast}, :#{#product.calidadObj}, :#{#product.color}, " +
            ":#{#product.degrWfe}, :#{#product.descripcion}, :#{#product.empPrimario}, " +
            ":#{#product.estado}, :#{#product.especie}, :#{#product.factorSp}, :#{#product.familia}, " +
            ":#{#product.forma}, :#{#product.harina}, :#{#product.mandt}, :#{#product.mesano}, " +
            ":#{#product.netoWfe}, :#{#product.rendimiento}, :#{#product.scale}, " +
            ":#{#product.scrapeMeat}, :#{#product.skin}, :#{#product.sobrepeso}, " +
            ":#{#product.subpWfe}, :#{#product.target}, :#{#product.tipo}, :#{#product.ultPerProd}, " +
            ":#{#product.wfeBkBp}, :#{#product.wfeBkHarasu}, :#{#product.wfeHarina}, " +
            ":#{#product.wfeMeat})")
    Mono<Void> insertProductData(ProductData product);

}
