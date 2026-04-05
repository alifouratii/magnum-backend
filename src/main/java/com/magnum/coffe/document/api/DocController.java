package com.magnum.coffe.document.api;



import com.magnum.coffe.document.dao.model.Doc;
import com.magnum.coffe.document.service.DocService;
import com.magnum.coffe.document.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DocController {
    private final DocService docService;
    private final FileService fileService;


    @GetMapping("/doc/{id}")
    public ResponseEntity<Doc> getDocument(@PathVariable String id) {
        Doc doc = docService.getDocById(id);
        return ResponseEntity.ok(doc);
    }

    //USED IN THE WEB
    @PostMapping("/upload")
    public ResponseEntity<Doc> upload(@RequestParam("file") MultipartFile file, @RequestParam(required = false) String fileName, @RequestParam(required = false) String docType) throws IOException {
        Doc doc = new Doc();
        if (file.getSize() >= 10485760) {

           return  ResponseEntity.badRequest().build();

        }else  {
            doc = fileService.createFileFromUpload(file, fileName,docType);

            return  ResponseEntity.ok().body(doc);

        }


    }
    @GetMapping("/download/{id}")
    public ResponseEntity<Doc>  download(@PathVariable String id) throws IOException {
        if(id!=null ){
            try {
                Doc loadFile = fileService.downloadFile(id);
                return ResponseEntity.status(HttpStatus.OK).body(loadFile);


            } catch (NoSuchElementException e) {
                // Document non trouvé
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }


        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

    }

}
