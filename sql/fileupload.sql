alter 'board'.'board'
      add column 'file_name' varchar(150) null after 'content',
        add column 'file_path' varchar(300) null after 'file_name',