package com.magnum.coffe.document.api;



import com.magnum.coffe.document.dao.model.File;
import com.magnum.coffe.document.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/file/{id}")
    public ResponseEntity<File> getFile(@PathVariable String id) {
        File file = fileService.getFileById(id);
        return ResponseEntity.ok(file);
    }

}
