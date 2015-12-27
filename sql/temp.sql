SELECT * FROM textcontext t where t.context like '%雅安%' and t.rowunm<100

delete FROM textcontext


select current_date

select now();

select count(1) from textcontext where title = ''

-- MYSQL删除重复记录(正解） 不报You can't specify target table 'textcontext' for update in FROM clause
delete a FROM textcontext a ,(
  SELECT * FROM textcontext GROUP BY title  having count(1)>1
) b where a.title=b.title and a.recId > b.recId;


select count(1) from textcontext where titleUrl='/articles/f6zYfi';
update textcontext set type=5
