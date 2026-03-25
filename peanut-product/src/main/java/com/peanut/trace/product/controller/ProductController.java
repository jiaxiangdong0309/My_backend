package com.peanut.trace.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.peanut.trace.common.entity.PeanutProduct;
import com.peanut.trace.common.result.R;
import com.peanut.trace.product.dto.ProductQueryRequest;
import com.peanut.trace.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

/**
 * 产品信息接口
 */
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 单条录入产品信息
     * POST /api/product/add
     * 权限：企业用户及以上
     */
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN','ENTERPRISE')")
    public R<PeanutProduct> add(@Valid @RequestBody PeanutProduct product) {
        return R.ok(productService.addProduct(product));
    }

    /**
     * 批量导入（Excel/CSV）
     * POST /api/product/batch-import
     */
    @PostMapping("/batch-import")
    @PreAuthorize("hasAnyRole('ADMIN','ENTERPRISE')")
    public R<Map<String, Object>> batchImport(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return R.fail("文件不能为空");
        }
        return R.ok(productService.batchImport(file));
    }

    /**
     * 多条件分页查询
     * GET /api/product/query
     */
    @GetMapping("/query")
    public R<IPage<PeanutProduct>> query(ProductQueryRequest req) {
        return R.ok(productService.queryPage(req));
    }

    /**
     * 根据批次号查询（详情）
     * GET /api/product/{batchNo}
     */
    @GetMapping("/{batchNo}")
    public R<PeanutProduct> getByBatchNo(@PathVariable String batchNo) {
        PeanutProduct product = productService.getByBatchNo(batchNo);
        if (product == null) {
            return R.fail(404, "产品不存在");
        }
        return R.ok(product);
    }

    /**
     * 扫码查询（公开接口，无需登录）
     * GET /api/product/scan/{code}
     */
    @GetMapping("/scan/{code}")
    public R<PeanutProduct> scan(@PathVariable String code) {
        PeanutProduct product = productService.getByBatchNo(code);
        if (product == null) {
            return R.fail(404, "未找到对应产品，请确认编码正确");
        }
        return R.ok(product);
    }

    /**
     * 修改产品信息
     * PUT /api/product/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ENTERPRISE')")
    public R<?> update(@PathVariable Long id, @RequestBody PeanutProduct product) {
        product.setId(id);
        productService.updateById(product);
        return R.ok();
    }

    /**
     * 删除产品（软删除）
     * DELETE /api/product/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public R<?> delete(@PathVariable Long id) {
        productService.removeById(id);
        return R.ok();
    }
}
